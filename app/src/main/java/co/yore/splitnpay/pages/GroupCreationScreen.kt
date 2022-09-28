package co.yore.splitnpay.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.libs.sep
import co.yore.splitnpay.models.Friend
import co.yore.splitnpay.ui.theme.DarkBlue
import com.rudra.yoresplitbill.R
import com.rudra.yoresplitbill.common.Constant
import com.rudra.yoresplitbill.common.FontFamilyText
import com.rudra.yoresplitbill.common.dep
import com.rudra.yoresplitbill.common.sep
import com.rudra.yoresplitbill.data.Friend
import com.rudra.yoresplitbill.ui.split.component.CustomButton_3egxtx
import com.rudra.yoresplitbill.ui.split.component.PhotoSelectionBottomSheet
import com.rudra.yoresplitbill.ui.split.group.splitdetails.TopBarWithIcon_1t9xbo
import com.rudra.yoresplitbill.ui.theme.DarkBlue
import com.rudra.yoresplitbill.ui.theme.LightBlue5
import com.rudra.yoresplitbill.ui.theme.LightGrey5
import com.rudra.yoresplitbill.ui.theme.SheetScrim
import faker.org.yaml.snakeyaml.scanner.Constant
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun GroupCreationScreen(
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = { false }
    )

    val groupImage = remember { mutableStateOf(R.drawable.ic_people) }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            /*PhotoSelectionBottomSheet(sheetState, coroutineScope){
                groupImage.value = R.drawable.person
            }*/
        },
        sheetShape = RoundedCornerShape(
            topStart = 25f.dep(),
            topEnd = 25f.dep()
        ),
        scrimColor = SheetScrim
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                // Top bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54f.dep()),
                    contentAlignment = Alignment.Center
                ) {
                    TopBarWithIcon_1t9xbo(
                        onClick = {
                            navController.navigateUp()
                        },
                        text = "Split"
                    )
                }

                Spacer(modifier = Modifier.height(32.dep()))

                //Image picker
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    GroupImagePicker_ncbmdg(
                        contentDescription = "Image Picker",
                        onClick = {
                            coroutineScope.launch {
                                sheetState.show()
                            }
                        },
                        groupImage = groupImage.value
                    )
                }

                Spacer(modifier = Modifier.height(32.dep()))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dep())
                ) {
                    FontFamilyText(
                        modifier = Modifier,
                        text = "Name Your Group",
                        fontSize = 16.sep(),
                        fontWeight = FontWeight(700),
                        color = DarkBlue
                    )
                }

                Spacer(modifier = Modifier.height(17.dep()))

                //Group name text field
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dep())
                        .padding(start = 16.dep(), end = 16.dep())
                ) {
                    GroupNameTextField()
                }

                Spacer(modifier = Modifier.height(23.dep()))

                //Selected people row
                Box(modifier = Modifier.padding(start = 16.dep(), end = 16.dep())) {
                    val friends = remember {
                        mutableStateListOf<Friend>(
                            Friend(
                                name = "Manisha Roy",
                                mobileNumber = "95633376942",
                                imageUrl = ""
                            ),
                            Friend(
                                name = "Sushil Roy",
                                mobileNumber = "95633376942",
                                imageUrl = ""
                            ),
                            Friend(
                                name = "Ankita Roy",
                                mobileNumber = "95633376942",
                                imageUrl = ""
                            ),
                            Friend(
                                name = "Banhi Sen",
                                mobileNumber = "95633376942",
                                imageUrl = ""
                            )
                        )
                    }
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(15.dep())
                    ) {
                        items(friends) { item ->
                            PeopleImageItem_r02b97(
                                onClick = {

                                },
                                onDelete = {
                                    friends.remove(item)
                                },
                                friend = item,
                                contentDescription = "people image"
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dep(),
                            end = 16.dep()
                        )
                        .clip(RoundedCornerShape(12.dep()))
                        .background(color = LightGrey5),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 11.dep(),
                                bottom = 11.dep(),
                                start = 19.dep(),
                                end = 14.dep()
                            ),
                        text = "*By continuing, you agree that members of this groups will abide by the Yore Payments user policies",
                        fontSize = 10.sep(),
                        fontWeight = FontWeight(400),
                        color = LightBlue5,
                        maxLines = 2,
                        lineHeight = 13.sep(),
                        fontFamily = Constant.robotoFonts
                    )
                }

                Spacer(modifier = Modifier.height(13.dep()))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(47f.dep())
                        .padding(
                            start = 16.dep(),
                            end = 16.dep()
                        )
                ) {
                    CustomButton_3egxtx(
                        text = "Continue",
                        onClick = {
                            navController.navigate("split_details_screen")
                        },
                        contentDescription = "Continue Button"
                    )
                }
                Spacer(modifier = Modifier.height(17.dep()))
            }
        }
    }
}

