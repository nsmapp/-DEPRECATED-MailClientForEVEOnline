package by.nepravskysm.mailclientforeveonline.presentation.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.usecase.auth.AuthUseCase
import by.nepravskysm.domain.usecase.character.GetActivCharInfoUseCase


class MainViewModel(private val authUseCase: AuthUseCase,
                    private val getActivCharInfoUseCase: GetActivCharInfoUseCase
): ViewModel(){



    val characterName: MutableLiveData<String> by lazy { MutableLiveData<String>("")}
    val characterId: MutableLiveData<Long> by lazy { MutableLiveData<Long>()}

    init {
        getActiveCharacterInfo()
    }

    fun startAuth(code: String){
        authUseCase.setFitstAuthToken(code)
        authUseCase.execute {
            onComplite {
                characterName.value = it.characterName
            }
            onError { Log.d("logde----->", it.toString()) }
        }
    }

    fun getActiveCharacterInfo(){
        getActivCharInfoUseCase.execute {
            onComplite {
                characterName.value = it.characterName
                characterId.value = it.characterId
            }
            onError { Log.d("logde----->", it.toString()) }
        }
    }
}