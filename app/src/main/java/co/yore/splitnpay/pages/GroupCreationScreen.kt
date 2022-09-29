package co.yore.splitnpay.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import co.yore.splitnpay.R
import co.yore.splitnpay.addmembers.FontFamilyText
import co.yore.splitnpay.libs.*
import co.yore.splitnpay.models.ContactData
import co.yore.splitnpay.models.DataIds
import co.yore.splitnpay.ui.theme.DarkBlue
import co.yore.splitnpay.ui.theme.robotoFonts

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun GroupCreationScreen(

) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmStateChange = { true },

    )



    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            /*PhotoSelectionBottomSheet(sheetState, coroutineScope){
                groupImage.value = R.drawable.person
            }*/
                       Text("")
        },
        sheetShape = RoundedCornerShape(
            topStart = 25f.dep(),
            topEnd = 25f.dep()
        ),
        scrimColor = Color(0x8C243257)
    ) {
        GroupCreationPageContent()
    }
}

@Composable
fun GroupCreationPageContent(
    friends: List<ContactData> = listState(DataIds.contacts),
    groupName: String = stringState(DataIds.groupName).value,
    notifier: NotificationService = notifier()
) {
    val coroutineScope = rememberCoroutineScope()
    val groupImage = remember { mutableStateOf(R.drawable.ic_people) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54f.dep()),
                contentAlignment = Alignment.Center
            ) {
                TopBarWithIcon_1t9xbo(
                    onClick = {
                        notifier.notify(DataIds.back)
                    },
                    text = "Split"
                )
            }

            Spacer(modifier = Modifier.height(32.dep()))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                GroupImagePicker_ncbmdg(
                    contentDescription = "Image Picker",
                    onClick = {
                        notifier.notify(DataIds.pickImage)
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
                GroupNameTextField(
                    groupName
                ){
                    notifier.notify(DataIds.groupName,it)
                }
            }

            Spacer(modifier = Modifier.height(23.dep()))

            //Selected people row
            Box(modifier = Modifier.padding(start = 16.dep(), end = 16.dep())) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dep())
                ) {
                    items(friends) { item ->
                        PeopleImageItem_r02b97(
                            onDelete = {
                                //friends.remove(item)
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
                    .background(color = Color(0xffE0E6ED)),
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
                    color = Color(0xff5A87BB),
                    maxLines = 2,
                    lineHeight = 13.sep(),
                    fontFamily = robotoFonts
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
                        //navController.navigate("split_details_screen")
                    },
                    contentDescription = "Continue Button"
                )
            }
            Spacer(modifier = Modifier.height(17.dep()))
        }
    }
}

