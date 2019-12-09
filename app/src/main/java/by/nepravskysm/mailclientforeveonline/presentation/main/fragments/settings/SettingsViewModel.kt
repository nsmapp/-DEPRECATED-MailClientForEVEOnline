package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.settings


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.usecase.character.UpdateContactsDBUseCase
import by.nepravskysm.domain.usecase.character.UpdateContactsRestUseCase

class SettingsViewModel(
    private val updateContactDB: UpdateContactsDBUseCase,
    private val updateContactRest: UpdateContactsRestUseCase
) : ViewModel() {

    val updateContactIsActive: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }


    fun updateContacts() {
        updateContactIsActive.value = true
        updateContactRest.execute {
            onComplite {
                updateContactDB.execute {
                    onComplite { updateContactIsActive.value = false }
                    onError { updateContactIsActive.value = false }
                }
            }
            onError { updateContactIsActive.value = false }
        }

    }
}