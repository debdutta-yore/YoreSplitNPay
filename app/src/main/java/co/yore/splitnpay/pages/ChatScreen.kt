package co.yore.splitnpay.pages

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import co.yore.splitnpay.R
import co.yore.splitnpay.components.components.*
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.libs.jerokit.*
import co.yore.splitnpay.models.*
import co.yore.splitnpay.models.BillTransaction
import co.yore.splitnpay.ui.theme.*
import co.yore.splitnpay.viewModels.*
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    // searchText: String = stringState(key = DataIds.searchText).value,
    notifier: NotificationService = notifier(),
    onFilter: () -> Unit
) {
    val colors = TextFieldDefaults.textFieldColors(
        disabledTextColor = Color.Black,
        backgroundColor = Color.White,
        focusedIndicatorColor = Alto,
        unfocusedIndicatorColor = Alto,
        disabledIndicatorColor = Alto,
        cursorColor = LightGray
    )
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        enabled = false,
        value = "",
        onValueChange = { notifier.notify(DataIds.searchText, it)},
        modifier = modifier
            .background(
                color = colors.backgroundColor(true).value,
                shape = TextFieldDefaults.TextFieldShape
            )
            .indicatorLine(
                enabled = true,
                isError = false,
                interactionSource = interactionSource,
                colors = colors
            ),
        interactionSource = interactionSource
    ) { innerTextField ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ){
            TextFieldDefaults.TextFieldDecorationBox(
                value = "",
                innerTextField = innerTextField,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(bottom = 9.dep()),
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                enabled = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.split_payments),
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sep(),
                        color = Color.Black,
                        letterSpacing = 0.17.sep(),
                        lineHeight = 25.sep(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = colors,
                trailingIcon = {
                    Row {
                        Spacer(
                            modifier = Modifier
                                .width(114.dep())
                        )
                        Icon(
                            modifier = Modifier
                                .size(33.dep())
                                .clip(CircleShape)
                                .clickable {
                                    notifier.notify(DataIds.search)
                                }
                                .padding(7.dep()),
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "search",
                            tint = Color.Unspecified
                        )
                        7.sx()
                        Icon(
                            modifier = Modifier
                                .size(33.dep())
                                .clip(CircleShape)
                                .clickable {
                                    onFilter()
                                }
                                .padding(6.dep()),
                            painter = painterResource(id = R.drawable.filter_icon),
                            contentDescription = "filter",
                            tint = Color.Unspecified
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun SearchField(
    config: ContactSearchBarConfiguration = ContactSearchBarConfiguration(),
    contentDescription: String,
    text: String,
    placeholderText: String,
    notifier: NotificationService = notifier()
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .semantics {
                this.contentDescription = contentDescription
            }
    ) {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(key1 = focusRequester){
            focusRequester.requestFocus()
        }
        TextField(
            modifier = Modifier
                .semantics {
                    this.contentDescription = "${contentDescription}_text_field"
                }
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .height(config.height.dep())
                .background(
                    config.backgroundColor,
                    RoundedCornerShape(config.borderRadius.dep())
                ),
            value = text,
            onValueChange = {
                notifier.notify(DataIds.searchTextInput, it)
            },
            textStyle = TextStyle(fontSize = config.fontSize.sep()),
            shape = RoundedCornerShape(config.borderRadius.dep()),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                cursorColor = config.cursorColor
            ),
            leadingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    config.iconLeftSpace.sx()
                    Icon(
                        painter = painterResource(id = config.searchIconId),
                        contentDescription = "search",
                        tint = config.searchIconTint
                    )
                    config.iconRightSpace.sx()
                    Divider(
                        color = config.dividerColor,
                        modifier = Modifier
                            .height(config.dividerHeight.dep())
                            .width(config.dividerWidth.dep())
                    )
                    config.dividerRightSpace.sx()
                }
            }
        )

        AnimatedVisibility(
            visible = text.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = placeholderText,
                fontSize = config.fontSize.sep(),
                color = config.color,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = config.startPadding.dep())
            )
        }
    }
}

@Composable
fun GroupSearchBar(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .layoutId("search_bar")
            .padding(horizontal = 16.dep())
    ) {
        SearchField(
            contentDescription = "split_search",
            text = text,
            placeholderText = stringResource(id = R.string.search)
        )
    }
}

@Composable
fun ContentUI(
    conversations: List<Conversation>,
    notifier: NotificationService
) {
    val lazyScrollState = rememberLazyListState()
    Box(
        modifier = Modifier
            .layoutId("content")
            .clip(RoundedCornerShape(24.dep()))
            .fillMaxWidth()
            .background(
                color = Zircon,
                shape = RoundedCornerShape(24.dep())
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 18.dep())
            ) {
                LazyColumn(
                    state = lazyScrollState,
                    verticalArrangement = Arrangement.spacedBy(8.dep()),
                    contentPadding = PaddingValues(
                        bottom = 80.dep(),
                        top = 18.dep()
                    ),
                    modifier = Modifier.fadingEdge(
                        startingColor = Zircon
                    )
                ) {
                    items(conversations) { it ->
                        ConversationItemUI(
                            it,
                            notifier
                        )
                    }
                }
            }
            42.sy()
        }
    }
}

@Composable
fun HeaderUI(
    groupImageString: String,
    modifier: Modifier,
    groupNameString: String,
    progress: Float,
    amountOrMobile: Any,
    notifier: NotificationService,
    searching: Boolean
) {
    val alpha by remember(searching) {
        derivedStateOf {
            if (searching){
                0f
            } else {
                1f
            }
        }
    }
    val animatedAlpha by animateFloatAsState(targetValue = alpha)
    if (alpha > 0f){
        UpperCut(
            modifier = Modifier
                .layoutId("upperCut")
                .alpha(animatedAlpha),
            color = Turquoise1
        )

        BottomCut(
            modifier = Modifier
                .layoutId("bottomCut")
                .alpha(animatedAlpha),
            color = Turquoise1
        )

        AsyncImage(
            modifier = Modifier
                .layoutId("groupImage")
                .coloredShadow(
                    color = VeniceBlue4F,
                    offsetX = 0.dep(),
                    offsetY = 2.25.dep(),
                    blurRadius = 4.5.dep(),
                    borderRadius = 100.dep(),
                    spread = 0f
                )
                .size(51.dep())
                .border(
                    width = 2.25.dep(),
                    color = Color.White,
                    shape = CircleShape
                )
                .padding(2.25.dep())
                .clip(CircleShape)
                .alpha(animatedAlpha),
            model = ImageRequest.Builder(LocalContext.current)
                .data(groupImageString)
                .crossfade(true).build(),
            placeholder = painterResource(R.drawable.personactionbar),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        FontFamilyText(
            modifier = modifier
                .layoutId("groupName")
                .alpha(animatedAlpha),
            text = groupNameString,
            fontSize = minMaxRangeValue(
                percentage = ((1 - progress) * 100),
                min = 11f,
                max = 16f
            ).sep(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 0.166667.sep()
        )
        if (amountOrMobile is Float){
            FontFamilyText(
                modifier = modifier
                    .layoutId("amount")
                    .alpha(animatedAlpha),
                annotatedString = amountOrMobile.amountAnnotatedString(
                    wholeNumberTextColor = Color.White,
                    decNumberTextColor = Color.White,
                    currencyTextColor = Color.White,
                    currencyFontSize = 21f,
                    wholeNumberFontSize = minMaxRangeValue(
                        percentage = (1 - progress) * 100,
                        max = 30f,
                        min = 24f
                    ),
                    wholeNumberFontWeight = FontWeight.Bold,
                    decNumberFontSize = 14f
                )
            )
        } else if (amountOrMobile is String){
            FontFamilyText(
                modifier = modifier
                    .layoutId("amount")
                    .alpha(animatedAlpha),
                text = amountOrMobile,
                fontSize = 14.sep(),
                color = Color.White
            )
        }

        SingleButton(
            modifier = Modifier
                .layoutId("settleCircle")
                .alpha(animatedAlpha),
            painter = painterResource(id = R.drawable.rupee_sign),
            onClick = {
                // navController.navigate("split_card_details_screen")
                notifier.notify(DataIds.settleClick)
            },
            backgroundColor = Color.Companion.blend(
                color1 = Color.Yellow,
                color2 = Color.White,
                progress = progress
            ),
            progress = progress
        )

        FontFamilyText(
            modifier = Modifier
                .layoutId("settleText")
                .scale(1 - progress)
                .alpha(1 - progress)
                .alpha(animatedAlpha),
            text = stringResource(R.string.settle),
            fontSize = 11.sep(),
            color = WildBlueYonder,
            lineHeight = 12.89.sep(),
            letterSpacing = 0.2.sep()
        )

        SingleButton(
            modifier = Modifier
                .layoutId("summaryCircle")
                .alpha(animatedAlpha),
            painter = painterResource(id = R.drawable.statement_icon),
            onClick = {
                // navController.navigate("split_summary_balance")
                notifier.notify(DataIds.summaryClick)
            },
            progress = progress
        )

        FontFamilyText(
            modifier = Modifier
                .layoutId("summaryText")
                .scale(1 - progress)
                .alpha(1 - progress)
                .alpha(animatedAlpha),
            text = stringResource(R.string.summary),
            fontSize = 11.sep(),
            color = WildBlueYonder,
            lineHeight = 12.89.sep(),
            letterSpacing = 0.2.sep()
        )

        SingleButton(
            modifier = Modifier
                .layoutId("manageCircle")
                .alpha(animatedAlpha),
            painter = painterResource(id = R.drawable.manage_icon),
            onClick = {
                notifier.notify(DataIds.manageClick)
            },
            progress = progress
        )

        FontFamilyText(
            modifier = Modifier
                .layoutId("manageText")
                .scale(1 - progress)
                .alpha(1 - progress)
                .alpha(animatedAlpha),
            text = stringResource(R.string.Manage),
            fontSize = 11.sep(),
            color = WildBlueYonder,
            lineHeight = 12.89.sep(),
            letterSpacing = 0.2.sep()
        )
    }
}

@SuppressLint("Range")
fun normalEndConstraint(
    dep: Dp,
    endGap: Float,
    animatedContentGap: Float
): ConstraintSet {
    return ConstraintSet {
        val upperCut = createRefFor("upperCut")
        val bottomCut = createRefFor("bottomCut")
        val content = createRefFor("content")
        val groupImage = createRefFor("groupImage")
        val groupName = createRefFor("groupName")
        val amount = createRefFor("amount")
        val settleCircle = createRefFor("settleCircle")
        val settleText = createRefFor("settleText")
        val summaryCircle = createRefFor("summaryCircle")
        val summaryText = createRefFor("summaryText")
        val menuOverlay = createRefFor("menuOverlay")
        val manageCircle = createRefFor("manageCircle")
        val manageText = createRefFor("manageText")
        val topBar = createRefFor("topBar")
        val bottomBar = createRefFor("bottomBar")
        val searchBar = createRefFor("searchBar")
        val tabs = createRefFor("tabs")
        val typingIndicator = createRefFor("typingIndicator")
        val searchField = createRefFor("searchField")

        constrain(searchField) {
            top.linkTo(parent.top, 71 * dep)
        }

        constrain(typingIndicator) {
            centerHorizontallyTo(parent)
            bottom.linkTo(bottomBar.top, 3 * dep)
        }
        constrain(topBar) {
            top.linkTo(parent.top)
        }

        constrain(bottomBar) {
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }

        constrain(searchBar) {
            top.linkTo(upperCut.bottom, 25 * dep)
            width = Dimension.fillToConstraints
        }

        constrain(tabs) {
            // top.linkTo(searchBar.bottom, 9 * dep)
            top.linkTo(parent.top, endGap * dep)
        }

        constrain(menuOverlay) {
            centerTo(summaryCircle)
        }
        constrain(summaryCircle) {
            top.linkTo(settleCircle.bottom, 2 * dep)
            end.linkTo(upperCut.end, 26 * dep)
            width = Dimension.value(4 * dep)
        }
        constrain(summaryText) {
            centerHorizontallyTo(summaryCircle)
            top.linkTo(summaryCircle.bottom)
            alpha = 0.0f
            scaleX = 0.0f
            scaleY = 0.0f
            pivotX = 0.5f
            pivotY = 0.05f
        }
        constrain(manageCircle) {
            top.linkTo(summaryCircle.bottom, 2 * dep)
            end.linkTo(upperCut.end, 26 * dep)
            width = Dimension.value(4 * dep)
        }
        constrain(manageText) {
            centerHorizontallyTo(manageCircle)
            top.linkTo(manageCircle.bottom)
            alpha = 0.0f
            scaleX = 0.0f
            scaleY = 0.0f
            pivotX = 0.5f
            pivotY = 0.05f
        }
        constrain(settleText) {
            centerHorizontallyTo(settleCircle)
            top.linkTo(settleCircle.bottom)
            alpha = 0.0f
            scaleX = 0.0f
            scaleY = 0.0f
            pivotX = 0.5f
            pivotY = 0.05f
        }
        constrain(upperCut) {
            height = Dimension.value(159 * dep)
        }
        constrain(bottomCut) {
            top.linkTo(upperCut.bottom)
        }
        constrain(content) {
            top.linkTo(tabs.bottom, animatedContentGap * dep)
            bottom.linkTo(parent.bottom)
            height = Dimension.fillToConstraints
        }
        constrain(groupImage) {
            centerVerticallyTo(upperCut)
            start.linkTo(upperCut.start, 37 * dep)
        }
        constrain(groupName) {
            top.linkTo(groupImage.top, 3 * dep)
            start.linkTo(groupImage.end, 12 * dep)
        }
        constrain(amount) {
            top.linkTo(groupName.bottom, 3 * dep)
            start.linkTo(groupName.start)
        }
        constrain(settleCircle) {
            top.linkTo(upperCut.top, 72 * dep)
            end.linkTo(upperCut.end, 26 * dep)
            width = Dimension.value(4 * dep)
        }
    }
}

fun normalStartConstraint(
    dep: Dp,
    startGap: Float
): ConstraintSet {
    return ConstraintSet {
        val upperCut = createRefFor("upperCut")
        val bottomCut = createRefFor("bottomCut")
        val content = createRefFor("content")
        val groupImage = createRefFor("groupImage")
        val groupName = createRefFor("groupName")
        val amount = createRefFor("amount")
        val settleCircle = createRefFor("settleCircle")
        val settleText = createRefFor("settleText")
        val summaryCircle = createRefFor("summaryCircle")
        val summaryText = createRefFor("summaryText")
        val menuOverlay = createRefFor("menuOverlay")
        val manageCircle = createRefFor("manageCircle")
        val manageText = createRefFor("manageText")
        val bottomBar = createRefFor("bottomBar")
        val topBar = createRefFor("topBar")
        val searchBar = createRefFor("searchBar")
        val tabs = createRefFor("tabs")
        val typingIndicator = createRefFor("typingIndicator")
        val searchField = createRefFor("searchField")

        constrain(searchField) {
            top.linkTo(parent.top, 71 * dep)
        }
        constrain(typingIndicator) {
            centerHorizontallyTo(parent)
            bottom.linkTo(bottomBar.top, 3 * dep)
        }
        constrain(topBar) {
            top.linkTo(parent.top)
        }
        constrain(bottomBar) {
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }
        constrain(searchBar) {
            top.linkTo(upperCut.bottom, 64 * dep)
            width = Dimension.fillToConstraints
        }
        constrain(tabs) {
            // top.linkTo(searchBar.bottom, 9 * dep)
            top.linkTo(parent.top, startGap * dep)
        }
        constrain(menuOverlay) {
            start.linkTo(upperCut.end)
            top.linkTo(upperCut.top, 68 * dep)
        }
        constrain(summaryCircle) {
            centerHorizontallyTo(upperCut)
            top.linkTo(upperCut.bottom, -23.5 * dep)
            width = Dimension.value(47 * dep)
        }
        constrain(summaryText) {
            centerHorizontallyTo(summaryCircle)
            top.linkTo(summaryCircle.bottom, 8 * dep)
        }
        constrain(manageCircle) {
            end.linkTo(upperCut.end, 72 * dep)
            top.linkTo(upperCut.bottom, -23.5 * dep)
            width = Dimension.value(47 * dep)
        }
        constrain(manageText) {
            centerHorizontallyTo(manageCircle)
            top.linkTo(manageCircle.bottom, 8 * dep)
        }
        constrain(upperCut) {
            height = Dimension.value(219 * dep)
        }
        constrain(bottomCut) {
            top.linkTo(upperCut.bottom)
        }
        constrain(content) {
            top.linkTo(tabs.bottom, 20 * dep)
            bottom.linkTo(parent.bottom)
            height = Dimension.fillToConstraints
        }
        constrain(groupImage) {
            centerHorizontallyTo(upperCut)
            top.linkTo(upperCut.top, 51 * dep)
        }
        constrain(groupName) {
            centerHorizontallyTo(groupImage)
            top.linkTo(groupImage.bottom, 14 * dep)
        }
        constrain(amount) {
            centerHorizontallyTo(groupImage)
            top.linkTo(groupName.bottom, 3 * dep)
        }
        constrain(settleCircle) {
            start.linkTo(upperCut.start, 72 * dep)
            top.linkTo(upperCut.bottom, -23.5 * dep)
            width = Dimension.value(47 * dep)
        }
        constrain(settleText) {
            centerHorizontallyTo(settleCircle)
            top.linkTo(settleCircle.bottom, 8 * dep)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TypingIndicator(
    modifier: Modifier = Modifier,
    typingMembers: List<Any?> = listState(key = DataIds.typingMembers)
) {
    Box(
        modifier =
        modifier
            .layoutId("typingIndicator")
    ){
        AnimatedVisibility(
            typingMembers.isNotEmpty(),
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ){
            Row(
                modifier = Modifier
                    .background(Color.White, CircleShape)
                    .padding(
                        start = 3.dep(),
                        end = 6.dep(),
                        top = 3.dep(),
                        bottom = 3.dep()
                    ),
                verticalAlignment = Alignment.CenterVertically
            ){
                GroupMemberProfilePics(
                    images = typingMembers,
                    config = GroupMemberProfilePicsConfiguration.smaller
                )
                4.sx()
                Text(
                    stringResource(id = R.string.typing),
                    color = CloudBurst,
                    fontSize = 9.sep()
                )
            }
        }
    }
}

@Composable
fun ConversationItemUI(
    it: Conversation,
    notifier: NotificationService
) {
    when (it.type){
        Conversation.Type.TRANSACTION -> {
            BillCard_s10zd7(
                transaction = it.data as BillTransaction,
                onClick = {
                    notifier.notify(DataIds.cardClick)
                }
            )
        }
        Conversation.Type.DATE -> {
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                FontFamilyText(
                    modifier = Modifier.align(Alignment.Center),
                    text = it.data as String,
                    fontSize = 14.sep(),
                    color = CuriousBlue
                )
            }

        }
        Conversation.Type.CREATION -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dep())
            ) {
                FontFamilyText(
                    modifier = Modifier.align(Alignment.Center),
                    annotatedString = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 13.sep(),
                                color = SteelBlue
                            )
                        ) {
                            append(stringResource(R.string.x_created_group, (it.data as GroupCreationEvent).creator))
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                fontSize = 13.sep(),
                                fontWeight = FontWeight.Bold,
                                color = SteelBlue
                            )
                        ) {
                            append((it.data as GroupCreationEvent).groupName)
                        }
                    },
                    color = SteelBlue,
                    fontSize = 13.sep()
                )
            }
        }
        Conversation.Type.STATUS -> {
            StatusUI(it)
        }
        Conversation.Type.CHAT -> {
            ChatItemUI((it.data as? ChatData) ?: return)
        }
        Conversation.Type.MEMBER -> {
            MemberUI(it.data as? MemberData ?: return)
        }
    }
}

@Composable
fun MemberUI(memberData: MemberData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dep()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .coloredShadowDp(
                    borderRadius = 16.5.dep(),
                    color = Black1A,
                    offsetX = 0.dep(),
                    offsetY = 4.dep(),
                    blurRadius = 4.dep(),
                    spread = 2.dep()
                )
                .size(33.dep())
                .clip(CircleShape)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 2.02.dep(),
                        color = Color.White,
                        shape = CircleShape
                    )
                    .padding(2.02.dep())
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(memberData.profileImage)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.user_dummy4),
                contentScale = ContentScale.Crop,
                contentDescription = "profile_image"
            )
        }
        8.sx()
        FontFamilyText(
            text = memberData.name,
            fontSize = 12.sep(),
            fontWeight = FontWeight.Bold,
            lineHeight = 14.06.sep(),
            letterSpacing = 0.2.sep()
        )
    }
}

@Composable
fun ChatItemUI(chatData: ChatData) {
    if (chatData.profileImage == null){
        MyChatUI(chatData.content)
    } else {
        PeerChatUI(chatData)
    }
}

@Composable
fun PeerChatUI(chatData: ChatData) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            AsyncImage(
                model = chatData.profileImage,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .size(30.dep())
                    .clip(CircleShape)
                    .border(
                        width = 3.dep(),
                        color = Zumthor,
                        shape = CircleShape
                    )
                    .padding(3.dep()),
                contentScale = ContentScale.Crop
            )
            7.sx()
            Box(
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(bottom = 10.67.dep())
                    // .width(154.dep())
                    .widthIn(max = 154.dep())
            ){

                Box(
                    modifier = Modifier
                        .padding(bottom = 2.5.dep(), start = 3.dep())
                        .widthIn(max = 151.dep())
                        .clip(RoundedCornerShape(10.dep()))
                        .background(Color.White)
                        .padding(12.dep()),
                    contentAlignment = Alignment.CenterStart
                ){
                    Text(
                        chatData.content as? String ?: "Not supported",
                        fontSize = 9.sep(),
                        color = CloudBurst,
                        textAlign = TextAlign.Start
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.my_chat_corner_shape),
                    contentDescription = "my_chat_corner_shape",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .size(18.5.dep())
                        .rotate(180f)
                )
            }
        }
    }
}

@Composable
fun MyChatUI(content: Any) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ){
        Box(
            modifier = Modifier
                .widthIn(max = 154.dep())
        ){
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 2.5.dep(), end = 3.dep())
                    .widthIn(max = 151.dep())
                    .clip(RoundedCornerShape(10.dep()))
                    .background(Botticelli)
                    .padding(12.dep()),
                contentAlignment = Alignment.CenterEnd
            ){
                Text(
                    content as? String ?: "Not supported",
                    fontSize = 9.sep(),
                    color = CloudBurst,
                    textAlign = TextAlign.End
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.my_chat_corner_shape),
                contentDescription = "my_chat_corner_shape",
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(18.5.dep())
            )
        }
    }
}

@Composable
fun StatusUI(
    it: Conversation
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if ((it.data as ChatStatus).left) Arrangement.Start else Arrangement.End
    ){
        Icon(
            painter = painterResource(id = R.drawable.status_seen),
            contentDescription = "",
            modifier = Modifier.size(16.dep()),
            tint = Color.Unspecified
        )
        3.sx()
        GroupMemberProfilePics(
            images = (it.data as ChatStatus).memberImages,
            config = GroupMemberProfilePicsConfiguration.smaller
        )
    }
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    alpha: Float,
    notifier: NotificationService = notifier(),
    text: String
) {
    Row(
        modifier = modifier
            .padding(top = 13.dep(), start = 9.dep(), end = 17.dep())
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(24.dep())
                .clip(CircleShape)
                .clickable {
                    notifier.notify(DataIds.back)
                }
                .padding(6.dep()),
            painter = painterResource(id = R.drawable.ic_left_arrow),
            tint = Color.blend(Color.White, Color.Black, 1 - alpha),
            contentDescription = "left arrow"
        )

        4.sx()

        FontFamilyText(
            text = text,
            fontSize = 14.sep(),
            fontWeight = FontWeight.Bold,
            color = Color.blend(Color.White, Color.Black, 1 - alpha),
            letterSpacing = (-0.333333).sep()
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier.size(18.dep()),
            painter = painterResource(id = R.drawable.ic_heart),
            tint = Color.blend(Color.White, Color.Black, 1 - alpha),
            contentDescription = "heart icon"
        )

        13.sx()

        Icon(
            modifier = Modifier
                .size(18.dep()),
            painter = painterResource(id = R.drawable.ic_share),
            tint = Color.blend(Color.White, Color.Black, 1 - alpha),
            contentDescription = "share icon"
        )
    }
}

@Composable
fun UpperCut(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = color,
                shape = RoundedCornerShape(
                    0.dp,
                    0.dp,
                    bottomEnd = 0f.dep(),
                    bottomStart = 48f.dep()
                )
            ),
        contentAlignment = Alignment.BottomCenter
    ) {

    }
}

@Composable
fun BottomCut(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dep())
            .background(color)
            .background(
                Color.White,
                RoundedCornerShape(
                    topEnd = 48.dep()

                )
            )
    ){

    }
}

@Composable
fun SingleButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    onClick: () -> Unit,
    backgroundColor: Color = White,
    progress: Float = 0f
) {
    Box(
        modifier = modifier
            .coloredShadow(
                color = HippieGreen07.copy(alpha = 0.31f),
                borderRadius = 50.dep(),
                blurRadius = 6.dep(),
                offsetX = 0.dep(),
                offsetY = 3.dep()
            )
            .size(
                minMaxRangeValue(
                    percentage = (1 - progress) * 100,
                    max = 47f,
                    min = 4f
                ).dep()
            )
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .width(16.dep())
                .scale(1 - progress)
                .alpha(1 - progress),
            painter = painter,
            tint = Color.Unspecified,
            contentDescription = "button section"
        )
    }
}

@Composable
fun GroupChatTabs_m5q49j(
    config: GroupChatTabsConfiguration = GroupChatTabsConfiguration(),
    selected: GroupChatTab = tState<GroupChatTab>(key = DataIds.groupChantTab).value,
    notifier: NotificationService = notifier()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(config.horizontalSpace.dep())
    ) {
        GroupChatTabItem_yb6b5a(
            selected = selected,
            text = "All",
            current = GroupChatTab.All,
            contentDescription = "tab",
            onClick = {
                notifier.notify(DataIds.groupChantTab, GroupChatTab.All)
            }
        )

        GroupChatTabItem_yb6b5a(
            selected = selected,
            text = "Pending",
            current = GroupChatTab.Pending,
            contentDescription = "tab",
            onClick = {
                notifier.notify(DataIds.groupChantTab, GroupChatTab.Pending)
            }
        )

        GroupChatTabItem_yb6b5a(
            selected = selected,
            text = "Settled",
            current = GroupChatTab.Settled,
            contentDescription = "tab",
            onClick = {
                notifier.notify(DataIds.groupChantTab, GroupChatTab.Settled)
            }
        )
    }
}

@Composable
fun GroupChatTabItem_yb6b5a(
    text: String,
    selected: GroupChatTab,
    current: GroupChatTab,
    contentDescription: String,
    config: SplitTabItemConfiguration = SplitTabItemConfiguration(),
    onClick: () -> Unit
) {
    val computedBackgroundColor by remember(selected) {
        derivedStateOf {
            if (selected == current) {
                config.selectedBackground
            } else {
                config.unSelectedBackground
            }
        }
    }
    val computedColor by remember(selected) {
        derivedStateOf {
            if (selected == current) {
                config.selectedTextColor
            } else {
                config.unSelectedTextColor
            }
        }
    }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = computedBackgroundColor,
        animationSpec = tween(1000)
    )
    val animatedColor by animateColorAsState(
        targetValue = computedColor,
        animationSpec = tween(1000)
    )
    Box(
        modifier = Modifier
            .semantics { this.contentDescription = contentDescription }
            .widthIn(config.minWidth.dep())
            .clip(RoundedCornerShape(config.roundedCorner.dep()))
            .background(animatedBackgroundColor)
            .clickable {
                onClick()
            }
            .padding(
                top = config.paddingTop.dep(),
                bottom = config.paddingBottom.dep(),
                start = config.paddingStart.dep(),
                end = config.paddingEnd.dep()
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = config.textSize.sep(),
            color = animatedColor,
            fontWeight = config.fontWeight
        )
    }
}

@Composable
fun Message(
    modifier: Modifier = Modifier,
    chatMessage: String = stringState(key = DataIds.chatMessage).value,
    notifier: NotificationService = notifier()
) {
    Row(
        modifier = modifier
            .navigationBarsPadding()
            .imePadding(),
        verticalAlignment = Alignment.Top
    ) {
        FloatingActionButton(
            shape = CircleShape,
            backgroundColor = RadicalRed,
            modifier = Modifier
                .size(47.dep())
                .coloredShadow(
                    color = RadicalRed4F,
                    offsetX = 0.dep(),
                    offsetY = 3.dep(),
                    blurRadius = 6.dep(),
                    borderRadius = 100.dep()
                ),
            onClick = {
                notifier.notify(DataIds.split)
            }
        ) {
            Icon(
                tint = White,
                painter = painterResource(id = R.drawable.ic_fab_icon),
                contentDescription = "fab icon"
            )
        }

        9.sx()

        BasicTextField(
            textStyle = TextStyle(
                fontSize = 12.sep(),
                color = Color.Black
            ),
            singleLine = true,
            maxLines = 1,
            value = chatMessage,
            onValueChange = {
                notifier.notify(DataIds.chatMessage, it)
            },
            decorationBox = {innerTextField ->
                Box(
                    Modifier
                        .padding(end = 18.dep(), bottom = 14.dep())
                        .fillMaxWidth()
                        .height(49.dep())
                        .coloredShadow(
                            color = Black1A,
                            offsetX = 0.dep(),
                            offsetY = 4.dep(),
                            blurRadius = 19.dep(),
                            borderRadius = 25.dep()
                        )
                        .background(Color.White, CircleShape)
                ){
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .padding(start = 18.dep())
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ){
                            innerTextField()
                        }
                        6.sx()
                        Icon(
                            modifier = Modifier
                                .padding(end = 14.dep())
                                .size(24.dep())
                                .clip(CircleShape)
                                .clickable {

                                }
                                .padding(4.dep()),
                            painter = painterResource(id = R.drawable.ic_send_icon),
                            tint = Emperor,
                            contentDescription = "send"
                        )
                    }
                    if (chatMessage.isEmpty()){
                        Text(
                            "Message",
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(horizontal = 20.dep()),
                            fontSize = 12.sep(),
                            color = WildBlueYonder
                        )
                    }

                }
            }
        )
    }
}
