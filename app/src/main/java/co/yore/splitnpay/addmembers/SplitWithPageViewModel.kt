package co.yore.splitnpay.addmembers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import co.yore.splitnpay.DataIds
import co.yore.splitnpay.NotificationService
import co.yore.splitnpay.Resolver
import com.rudra.yoresplitbill.ui.split.group.addmembers.ContactData
import com.rudra.yoresplitbill.ui.split.group.addmembers.GroupOrContact


class SplitWithPageViewModel: ViewModel() {
    val resolver = Resolver()
    private val splitWithInput = mutableStateOf("")
    private val groupOrContacts = mutableStateListOf<GroupOrContact>()
    private val addedContacts = mutableStateListOf<ContactData>()
    private val _notificationService = NotificationService{id,arg->
        when(id){
            DataIds.textInput->{
                splitWithInput.value = (arg as? String)?:return@NotificationService
            }
            DataIds.checkItem->{
                val item = (arg as? GroupOrContact)?:return@NotificationService
                if(item is ContactData){
                    val index = groupOrContacts.indexOf(item)
                    val finalChecked = !item.selected
                    if(finalChecked){
                        addedContacts.add(item)
                    }
                    else{
                        val ind = addedContacts.indexOfFirst { it.id==item.id }
                        addedContacts.removeAt(ind)
                    }
                    groupOrContacts[index] = item.copy(selected = !item.selected)
                }
            }
        }
    }
    val notifier = _notificationService
    init {
        resolver[DataIds.textInput] = splitWithInput
        resolver[DataIds.groupOrContact] = groupOrContacts
        resolver[DataIds.addedContacts] = addedContacts
        var i = 0
        groupOrContacts.addAll(MutableList(20){
            ContactData(
                id = ++i,
                name = "Debdutta Panda",
                mobile = "8967114927",
                image = "https://i.pravatar.cc/100",
            )
        })
    }
}