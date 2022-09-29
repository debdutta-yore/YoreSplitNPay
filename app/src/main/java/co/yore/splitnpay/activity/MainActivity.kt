package co.yore.splitnpay.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.constraintlayout.compose.*
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yore.splitnpay.components.components.CollapsibleBox
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.locals.RobotoText
import co.yore.splitnpay.locals.localDesignWidth
import co.yore.splitnpay.pages.GroupCreationScreen
import co.yore.splitnpay.pages.MemberSelectionPage_g5024t
import co.yore.splitnpay.pages.SplitPage
import co.yore.splitnpay.ui.theme.YoreSplitNPayTheme
import co.yore.splitnpay.viewModels.GroupCreationPageViewModel
import co.yore.splitnpay.viewModels.MemberSelectionPageViewModel
import co.yore.splitnpay.viewModels.SplitPageViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            YoreSplitNPayTheme {
                CompositionLocalProvider(localDesignWidth provides 360f) {
                    Surface(
                        modifier = Modifier
                            .safeDrawingPadding()
                            .fillMaxSize(),
                        color = MaterialTheme.colors.background,
                    ) {
                        val navController = rememberAnimatedNavController()
                        AnimatedNavHost(navController, startDestination = "split_page") {
                            yoreComposable(
                                "split_page"
                            ){
                                YorePage(
                                    navController,
                                    suffix = "split_page",
                                    wvm = viewModel<SplitPageViewModel>()
                                ) {
                                    SplitPage()
                                }
                            }
                            yoreComposable(
                                "split_with_page",
                            ){
                                YorePage(
                                    navController,
                                    suffix = "split_with_page",
                                    wvm = viewModel<MemberSelectionPageViewModel>()
                                ) {
                                    MemberSelectionPage_g5024t()
                                }
                            }

                            yoreComposable(
                                "group_creation"
                            ){
                                YorePage(
                                    navController,
                                    suffix = "group_creation",
                                    wvm = viewModel<GroupCreationPageViewModel>()
                                ) {
                                    GroupCreationScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

@SuppressLint("Range")
@OptIn(ExperimentalMotionApi::class)
@Composable
fun CutPage() {
    CollapsibleBox(
        threshold = 0.05f,
        modifier = Modifier
            .fillMaxSize()
    ) {
        val dep = 1.dep()
        MotionLayout(
            start = ConstraintSet {
                val upper_cut = createRefFor("upper_cut")
                val bottom_cut = createRefFor("bottom_cut")
                val content = createRefFor("content")
                val profile_image = createRefFor("profile_image")
                val name = createRefFor("name")
                val amount = createRefFor("amount")
                val settle_circle = createRefFor("settle_circle")
                val settle_text = createRefFor("settle_text")
                /////////////////
                val summary_circle = createRefFor("summary_circle")
                val summary_text = createRefFor("summary_text")
                val menu_overlay = createRefFor("menu_overlay")
                constrain(menu_overlay){
                    start.linkTo(upper_cut.end)
                    top.linkTo(upper_cut.top,68*dep)
                }
                constrain(summary_circle){
                    centerHorizontallyTo(upper_cut)
                    top.linkTo(upper_cut.bottom,-23.5*dep)
                    width = Dimension.value(47*dep)
                }
                constrain(summary_text){
                    centerHorizontallyTo(summary_circle)
                    top.linkTo(summary_circle.bottom,8*dep)
                }
                /////////////////
                val manage_circle = createRefFor("manage_circle")
                val manage_text = createRefFor("manage_text")
                constrain(manage_circle){
                    end.linkTo(upper_cut.end,72*dep)
                    top.linkTo(upper_cut.bottom,-23.5*dep)
                    width = Dimension.value(47*dep)
                }
                constrain(manage_text){
                    centerHorizontallyTo(manage_circle)
                    top.linkTo(manage_circle.bottom,8*dep)
                }
                ////////////
                constrain(upper_cut){
                    height = Dimension.value(219*dep)
                }
                constrain(bottom_cut){
                    top.linkTo(upper_cut.bottom)
                }
                constrain(content){
                    top.linkTo(upper_cut.bottom,91*dep)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                constrain(profile_image){
                    centerHorizontallyTo(upper_cut)
                    top.linkTo(upper_cut.top,51*dep)
                }
                constrain(name){
                    centerHorizontallyTo(profile_image)
                    top.linkTo(profile_image.bottom,14*dep)
                }
                constrain(amount){
                    centerHorizontallyTo(profile_image)
                    top.linkTo(name.bottom,3*dep)
                }
                constrain(settle_circle){
                    start.linkTo(upper_cut.start,72*dep)
                    top.linkTo(upper_cut.bottom,-23.5*dep)
                    width = Dimension.value(47*dep)
                }
                constrain(settle_text){
                    centerHorizontallyTo(settle_circle)
                    top.linkTo(settle_circle.bottom,8*dep)
                }
            },
            end = ConstraintSet {
                val upper_cut = createRefFor("upper_cut")
                val bottom_cut = createRefFor("bottom_cut")
                val content = createRefFor("content")
                val profile_image = createRefFor("profile_image")
                val name = createRefFor("name")
                val amount = createRefFor("amount")
                val settle_circle = createRefFor("settle_circle")
                val settle_text = createRefFor("settle_text")
                val menu_overlay = createRefFor("menu_overlay")
                val summary_circle = createRefFor("summary_circle")
                val summary_text = createRefFor("summary_text")
                constrain(menu_overlay){
                    centerTo(summary_circle)
                }
                ////////////////
                constrain(summary_circle){
                    top.linkTo(settle_circle.bottom,2*dep)
                    end.linkTo(upper_cut.end,26*dep)
                    width = Dimension.value(4*dep)
                }
                constrain(summary_text){
                    centerHorizontallyTo(summary_circle)
                    top.linkTo(summary_circle.bottom)
                    alpha = 0.0f
                    scaleX = 0.0f
                    scaleY = 0.0f
                    pivotX = 0.5f
                    pivotY = 0.05f
                }
                //////////////////
                val manage_circle = createRefFor("manage_circle")
                val manage_text = createRefFor("manage_text")
                constrain(manage_circle){
                    top.linkTo(summary_circle.bottom,2*dep)
                    end.linkTo(upper_cut.end,26*dep)
                    width = Dimension.value(4*dep)
                }
                constrain(manage_text){
                    centerHorizontallyTo(manage_circle)
                    top.linkTo(manage_circle.bottom)
                    alpha = 0.0f
                    scaleX = 0.0f
                    scaleY = 0.0f
                    pivotX = 0.5f
                    pivotY = 0.05f
                }
                /////////////////
                constrain(settle_text){
                    centerHorizontallyTo(settle_circle)
                    top.linkTo(settle_circle.bottom)
                    alpha = 0.0f
                    scaleX = 0.0f
                    scaleY = 0.0f
                    pivotX = 0.5f
                    pivotY = 0.05f
                }
                constrain(upper_cut){
                    height = Dimension.value(159*dep)
                }
                constrain(bottom_cut){
                    top.linkTo(upper_cut.bottom)
                }
                constrain(content){
                    top.linkTo(upper_cut.bottom,28*dep)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                constrain(profile_image){
                    centerVerticallyTo(upper_cut)
                    start.linkTo(upper_cut.start,37*dep)
                }
                constrain(name){
                    top.linkTo(profile_image.top,3*dep)
                    start.linkTo(profile_image.end,12*dep)
                }
                constrain(amount){
                    top.linkTo(name.bottom,3*dep)
                    start.linkTo(name.start)
                }
                constrain(settle_circle){
                    top.linkTo(upper_cut.top,72*dep)
                    end.linkTo(upper_cut.end,26*dep)
                    width = Dimension.value(4*dep)
                }
            },
            progress = it,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .layoutId("upper_cut")
                    .fillMaxWidth()
                    .background(
                        Color.Green, RoundedCornerShape(
                            bottomStart = 47.dep()
                        )
                    )
            ){

            }
            Box(
                modifier = Modifier
                    .layoutId("profile_image")
                    .size(51.dep())
                    .background(Color.White, CircleShape)
            ){

            }
            RobotoText(
                "Office Buddies",
                color = Color.White,
                fontSize = (11+5*(1f-it)).sep(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .layoutId("name")
            )
            RobotoText(
                "2000",
                color = Color.White,
                fontSize = (24+6*(1f-it)).sep(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .layoutId("amount")
            )
            /////////////////////////
            Box(
                modifier = Modifier
                    .layoutId("bottom_cut")
                    .fillMaxWidth()
                    .height(42.dep())
                    .background(Color.Green)
                    .background(
                        Color.White, RoundedCornerShape(
                            topEnd = 47.dep()
                        )
                    )
            ){

            }
            Box(
                modifier = Modifier
                    .layoutId("settle_circle")
                    .aspectRatio(1f)
                    .background(Color.blend(Color.Yellow,Color.White,it), CircleShape)
            ){

            }
            RobotoText(
                "Settle",
                fontSize = 11.sep(),
                modifier = Modifier
                    .layoutId("settle_text")
            )
            ///////////////////////////////
            Box(
                modifier = Modifier
                    .layoutId("summary_circle")
                    .aspectRatio(1f)
                    .background(Color.White, CircleShape)
            ){

            }
            RobotoText(
                "Summary",
                fontSize = 11.sep(),
                modifier = Modifier
                    .layoutId("summary_text")
            )
            ///////////////////////////////
            Box(
                modifier = Modifier
                    .layoutId("manage_circle")
                    .aspectRatio(1f)
                    .background(Color.White, CircleShape)
            ){

            }
            RobotoText(
                "Manage",
                fontSize = 11.sep(),
                modifier = Modifier
                    .layoutId("manage_text")
            )
            //////////////////////////////
            Box(
                modifier = Modifier
                    .layoutId("menu_overlay")
                    .size(24.dep())
                    .clip(CircleShape)
                    .clickable {  }
            ){

            }
            //////////////////////////////
            Box(
                modifier = Modifier
                    .layoutId("content")
                    .fillMaxWidth()
                    .background(Color.Red)
            ){

            }
        }
    }
}







