package by.nepravskysm.mailclientforeveonline.di

import by.nepravskysm.database.AppDatabase
import by.nepravskysm.database.repoimpl.ActiveCharacterRepoImpl
import by.nepravskysm.database.repoimpl.AuthInfoRepoImpl
import by.nepravskysm.database.repoimpl.DBCharacterContactsRepoImpl
import by.nepravskysm.database.repoimpl.DBMailHeaderRepoImpl
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.database.DBCharacterContactsRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.auth.CharacterInfoRepository
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository
import by.nepravskysm.domain.repository.rest.character.CharacterContactsRepository
import by.nepravskysm.domain.repository.rest.mail.MailingListRepository
import by.nepravskysm.domain.repository.rest.mail.MailRepository
import by.nepravskysm.domain.repository.rest.mail.MailsHeadersRepository
import by.nepravskysm.domain.repository.utils.IdsRepository
import by.nepravskysm.domain.repository.utils.NamesRepository
import by.nepravskysm.domain.usecase.auth.AuthUseCase
import by.nepravskysm.domain.usecase.auth.GetAllCharactersAuthInfoUseCase
import by.nepravskysm.domain.usecase.character.ChangeActiveCharacter
import by.nepravskysm.domain.usecase.character.GetActivCharInfoUseCase
import by.nepravskysm.domain.usecase.character.GetContactUseCase
import by.nepravskysm.domain.usecase.character.SynchroniseCharactersContactsUseCase
import by.nepravskysm.domain.usecase.mails.*
import by.nepravskysm.mailclientforeveonline.presentation.main.MainViewModel
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.MailListViewModel
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.newmail.NewMailViewModel
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.readmail.ReadMailViewModel
import by.nepravskysm.rest.api.AuthManager
import by.nepravskysm.rest.api.EsiManager
import by.nepravskysm.rest.entity.response.MailingListRepoImpl
import by.nepravskysm.rest.repoimpl.auth.AuthRepoImpl
import by.nepravskysm.rest.repoimpl.esi.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val restModule: Module = module {

    single { AuthManager() }
    single { EsiManager() }

    factory<AuthRepository> { AuthRepoImpl(authManager = get()) }
    factory<CharacterInfoRepository>{
        CharacterInfoRepoImpl(
            esiManager = get()
        )
    }

    factory<MailsHeadersRepository> { MailsHeadersRepoImpl(esiManager = get())  }
    factory<MailRepository> { MailRepoImpl(esiManager = get())  }
    factory<NamesRepository> { NamesRepoImpl(esiManager = get()) }
    factory<IdsRepository> {IdsRepoImpl(esiManager = get())}
    factory<MailingListRepository> {MailingListRepoImpl(esiManager = get())  }

    factory<CharacterContactsRepository>{CharacterContactsRepoImpl(esiManager = get())}

}

val databaseModule: Module = module {

    single<AppDatabase> { AppDatabase.getInstance(androidContext())}

    factory<AuthInfoRepository> {AuthInfoRepoImpl(appDatabase = get())}
    factory<DBMailHeadersRepository>{DBMailHeaderRepoImpl(appDatabase = get())}
    factory<ActiveCharacterRepository>{ActiveCharacterRepoImpl(appDatabase = get())}
    factory<DBCharacterContactsRepository> {DBCharacterContactsRepoImpl(appDatabase = get())}

}

val useCaseModule: Module = module {

    factory{ AuthUseCase(authRepository = get(),
        authInfoRepository = get(),
        characterInfoRepository = get(),
        activeCharacterRepository = get()
    )  }

    factory { SynchroMailsHeaderUseCase(authRepository = get(),
        authInfoRepository = get(),
        mailsHeadersRepository = get(),
        namesRepository = get(),
        dbMailHeadersRepository = get(),
        mailingListRepository = get(),
        activeCharacterRepository = get()
    ) }

    factory { GetNewMailHeadersUseCase(authRepository = get(),
        authInfoRepository = get(),
        mailsHeadersRepository = get(),
        namesRepository = get(),
        dbMailHeadersRepository = get(),
        mailingListRepository = get(),
        activeCharacterRepository = get()
    ) }

    factory { GetActivCharInfoUseCase(authInfoRepository = get(),
        activeCharacterRepository = get()) }
    factory { GetMailUseCase(authRepository = get(),
        authInfoRepository = get(),
        mailRepository = get(),
        activeCharacterRepository = get()
    ) }

    factory { SendMailUseCase(authRepository = get(),
        authInfoRepository = get(),
        mailRepository = get(),
        idsRepository = get(),
        activeCharacterRepository = get()
    ) }

    factory { UpdataMailMetadataUseCase(authRepository = get(),
        authInfoRepository = get(),
        mailRepository = get(),
        activeCharacterRepository = get()
    ) }

    factory { DeleteMailUseCaseFromServerUseCase(authRepository = get(),
        authInfoRepository = get(),
        mailRepository = get(),
        activeCharacterRepository = get()
    ) }

    factory { GetMailHeadersFromDB(dbMailHeadersRepository = get(),
        activeCharacterRepository = get()) }

    factory { UpdateDBMailMetadataUseCase( dbMailHeadersRepository = get())}

    factory { GetAllCharactersAuthInfoUseCase(authInfoRepository = get()) }

    factory { ChangeActiveCharacter( activeCharacterRepository = get()) }

    factory { GetNewMailCountUseCase(authInfoRepository = get(),
        activeCharacterRepository = get(),
        dbMailHeadersRepository = get(),
        authRepository = get(),
        mailsHeadersRepository = get()
    ) }

    factory { DeleteMailFromDBUseCase(dbMailHeadersRepository = get()) }

    factory { SynchroniseCharactersContactsUseCase(authInfoRepository = get(),
        activeCharacterRepository = get(),
        authRepository = get(),
        characterContactsRepository = get(),
        dbCharacterContactsRepository = get(),
        namesRepository = get()
    ) }

    factory { GetContactUseCase(activeCharacterRepository = get(),
        dbCharacterContactsRepository = get()
    ) }

}

val viewModelModule: Module = module {
    viewModel { MainViewModel(authUseCase = get(),
        getActivCharInfoUseCase = get(),
        synchroMailsHeaderUseCase = get(),
        synchroniseCharactersContactsUseCase = get()
    ) }

    viewModel { MailListViewModel(synchroMailsHeaderUseCase = get(),
        getMailHeadersFromDB = get(),
        getNewMailHeadersUseCase = get()
    )}

    viewModel { ReadMailViewModel(getMailUseCase = get(),
        updataMailMetadataUseCase = get(),
        updateDBMailMetadataUseCase = get(),
        deleteMailUseCaseFromServerUseCase = get(),
        deleteMailFromDBUseCase = get()
    )}

    viewModel { NewMailViewModel(sendMailUseCase = get()) }

}