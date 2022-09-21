package co.yore.splitnpay

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

private interface Content

private data class FromTo(
    val f: String,
    val t: String
): Content

private data class Only(
    val value: String
): Content

interface AnimatableTextPart{
    enum class Scheme{
        SEMANTIC,
        EFFICIENT,
        NONE
    }
    data class DiffSettings(
        val scheme: Scheme = Scheme.SEMANTIC,
        val editCost: Short = 4
    )
    companion object{
        fun diff(
            from: String,
            to: String,
            style: SpanStyle? = null,
            diffSettings: DiffSettings = DiffSettings()
        ): List<AnimatableTextPart>{
            val list = mutableListOf<Content>()
            var temp: diff_match_patch.Diff? = null
            val differ = diff_match_patch()
            ///////////////
            val diff = differ.diff_main(from,to)
            ////////////////////////////////
            when(diffSettings.scheme){
                Scheme.SEMANTIC -> differ.diff_cleanupSemantic(diff)
                Scheme.EFFICIENT -> {
                    differ.Diff_EditCost = diffSettings.editCost
                    differ.diff_cleanupEfficiency(diff)
                }
                Scheme.NONE -> {}
            }
            //////////////////////////////////
            diff.forEach {
                when(it.operation){
                    diff_match_patch.Operation.DELETE -> {
                        temp = if(temp==null){
                            it
                        } else{
                            list.add(FromTo(f = it.text,t=temp?.text?:""))
                            null
                        }
                    }
                    diff_match_patch.Operation.INSERT -> {
                        temp = if(temp==null){
                            it
                        } else{
                            list.add(FromTo(f = temp?.text?:"",t=it.text))
                            null
                        }
                    }
                    diff_match_patch.Operation.EQUAL -> {
                        if(temp!=null){
                            val r = temp!!
                            temp = null
                            if(r.operation==diff_match_patch.Operation.INSERT){
                                list.add(FromTo(f = "",t=r.text))
                            }
                            else{
                                list.add(FromTo(f = r.text,t=""))
                            }
                        }
                        list.add(Only(it.text))
                    }//
                }
            }
            if(temp!=null){
                val r = temp!!
                temp = null
                if(r.operation==diff_match_patch.Operation.INSERT){
                    list.add(FromTo(f = "",t=r.text))
                }
                else{
                    list.add(FromTo(f = r.text,t=""))
                }
            }
            return list.map {
                when(it){
                    is Only->{
                        NonAnimatableText(it.value,style)
                    }
                    is FromTo->{
                        AnimatableTextPair(start = it.f, end = it.t, style = style)
                    }
                    else->NonAnimatableText("",style)
                }
            }
        }

        val render: (AnnotatedString.Builder.(AnimatableTextPart,Float,SpanStyle?)->Unit) = {it,progress,spanStyle->
            if(it is NonAnimatableText){
                val _style = (it.style?:spanStyle)?: SpanStyle()
                withStyle(_style){
                    append(it.value)
                }
            }
            else if(it is AnimatableTextPair){
                if(progress<1f){
                    val _style = (it.style?:spanStyle)?:SpanStyle()
                    withStyle(
                        _style.copy(
                            textGeometricTransform = TextGeometricTransform(scaleX = 1f-progress)
                        )
                    ){
                        append(it.start)
                    }
                }
                if(progress>0f){
                    val _style = (it.style?:spanStyle)?:SpanStyle()
                    withStyle(
                        _style.copy(
                            textGeometricTransform = TextGeometricTransform(scaleX = progress)
                        )
                    ){
                        append(it.end)
                    }
                }
            }
        }
    }
}

data class AnimatableTextPair(
    val start: String,
    val end: String,
    val style: SpanStyle? = null
): AnimatableTextPart

data class NonAnimatableText(
    val value: String,
    val style: SpanStyle? = null
): AnimatableTextPart

@Composable
fun AnimatedTextContent(
    modifier: Modifier = Modifier,
    items: List<AnimatableTextPart>,
    spanStyle: SpanStyle? = null,
    progress: Float,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    renderer: (AnnotatedString.Builder.(AnimatableTextPart,Float,SpanStyle?)->Unit) = AnimatableTextPart.render
){
    Text(
        text = buildAnnotatedString {
            items.forEach {
                renderer(this,it,progress,spanStyle)
            }
        },
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        inlineContent = inlineContent,
        onTextLayout = onTextLayout,
        style = style,
    )
}

@Composable
fun AnimatedTextContent(
    modifier: Modifier = Modifier,
    from: String,
    to: String,
    diffSettings: AnimatableTextPart.DiffSettings = AnimatableTextPart.DiffSettings(),
    spanStyle: SpanStyle? = null,
    progress: Float,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    renderer: (AnnotatedString.Builder.(AnimatableTextPart,Float,SpanStyle?)->Unit) = AnimatableTextPart.render
){
    val items by remember(from,to,spanStyle) {
        derivedStateOf {
            AnimatableTextPart.diff(from,to,spanStyle,diffSettings)
        }
    }
    AnimatedTextContent(
        modifier = modifier,
        items = items,
        spanStyle = spanStyle,
        progress = progress,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        inlineContent = inlineContent,
        onTextLayout = onTextLayout,
        style = style,
        renderer = renderer
    )
}