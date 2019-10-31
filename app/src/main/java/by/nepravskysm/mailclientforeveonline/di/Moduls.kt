package by.nepravskysm.mailclientforeveonline.di

import by.nepravskysm.database.AppDatabase
import by.nepravskysm.database.repoimpl.ActiveCharacterRepoImpl
import by.nepravskysm.database.repoimpl.AuthInfoRepoImpl
import by.nepravskysm.database.repoimpl.DBCharacterContactsRepoImpl
import by.nepravskysm.database.repoimpl.DBMailHeaderRepoImpl
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.database.DBCharacterContactsRepository
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.auth.CharacterInfoRepository
import by.nepravskysm.domain.repository.rest.character.CharacterContactsRepository
import by.nepravskysm.domain.repository.rest.mail.MailRepository
import by.nepravskysm.domain.repository.rest.mail.MailingListRepository
import by.nepravskysm.domain.repository.rest.mail.MailsHeadersRepository
import by.nepravskysm.domain.repository.utils.IdsRepository
import by.nepravskysm.domain.repository.utils.NamesRepository
import by.nepravskysm.domain.usecase.auth.AuthUseCase
import by.nepravskysm.domain.usecase.auth.GetAllCharactersAuthInfoUseCase
import by.nepravskysm.domain.usecase.character.*
import by.nepravskysm.domain.usecase.mails.*
import by.nepravskysm.mailclientforeveonline.presentation.main.MainViewModel
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.alliance.AllianceViewModel
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.BaseMailListViewModel
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.corp.CorporationViewModel
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.inbox.InboxViewModel
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.mailinglist.MailingListViewModel
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.send.SendViewModel
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

    single<AuthInfoRepository> { AuthInfoRepoImpl(appDatabase = get()) }
    single<DBMailHeadersRepository> { DBMailHeaderRepoImpl(appDatabase = get()) }
    single<ActiveCharacterRepository> { ActiveCharacterRepoImpl(appDatabase = get()) }
    single<DBCharacterContactsRepository> { DBCharacterContactsRepoImpl(appDatabase = get()) }

}

val useCaseModule: Module = module {

    factory {
        AuthUseCase(
            authRepo = get(),
            authInfoRepo = get(),
            characterInfoRepo = get(),
            activeCharacterRepo = get()
    )  }

    factory {
        SynchroMailsHeaderUseCase(
            authRepo = get(),
            authInfoRepo = get(),
            mailsHeadersRepo = get(),
            namesRepo = get(),
            dbMailHeadersRepo = get(),
            mailingListRepo = get(),
            activeCharacterRepo = get()
    ) }

    factory {
        GetNewMailHeadersUseCase(
            authRepo = get(),
            authInfoRepo = get(),
            mailsHeadersRepo = get(),
            namesRepo = get(),
            dbMailHeadersRepo = get(),
            mailingListRepo = get(),
            activeCharacterRepo = get()
    ) }

    factory {
        GetActivCharInfoUseCase(
            authInfoRepo = get(),
            activeCharacterRepo = get()
        )
    }
    factory {
        GetMailUseCase(
            authRepo = get(),
            authInfoRepo = get(),
            mailRepo = get(),
            activeCharacterRepo = get()
    ) }

    factory {
        SendMailUseCase(
            authRepo = get(),
            authInfoRepo = get(),
            mailRepo = get(),
            idsRepo = get(),
            activeCharacterRepo = get()
    ) }

    factory {
        UpdataMailMetadataUseCase(
            authRepo = get(),
            authInfoRepo = get(),
            mailRepo = get(),
            activeCharacterRepo = get()
    ) }

    factory {
        DeleteMailFromServerUseCase(
            authRepo = get(),
            authInfoRepo = get(),
            mailRepo = get(),
            activeCharacterRepo = get()
    ) }

    factory {
        GetMailHeadersFromDBUseCase(
            dbMailHeadersRepo = get(),
            activeCharacterRepo = get()
        )
    }

    factory { UpdateDBMailMetadataUseCase(dbMailHeadersRepo = get()) }

    factory { GetAllCharactersAuthInfoUseCase(authInfoRepo = get()) }

    factory { ChangeActiveCharacter(activeCharacterRepo = get()) }

    factory { DeleteMailFromDBUseCase(dbMailHeadersRepo = get()) }

    factory {
        UpdateContactsRestUseCase(
            authInfoRepo = get(),
            activeCharacterRepo = get(),
            dbCharacterContactsRepo = get(),
            characterContactsRepo = get(),
            namesRepo = get()
        )
    }

    factory { GetContactFromDBUseCase(dbCharacterContactsRepo = get()) }

    factory { UpdateContactsDBUseCase(dbMailHeadersRepo = get(),
        activeCharacterRepo = get(),
        dbCharacterContactsRepo = get())
    }

    factory { GetMailHeadersAfterIdFromDBUseCase(dbMailHeadersRepo = get(),
        activeCharacterRepo = get()) }

    factory { GetUnreadMailUseCase(activeCharacterRepo = get(),
        dbMailHeadersRepo = get()) }

}

val viewModelModule: Module = module {
    viewModel { MainViewModel(authUseCase = get(),
        getActiveCharInfo = get(),
        synchrMailsHeader = get(),
        updateContactsRest = get(),
        updateContactsDB = get(),
        getUnreadMailCount = get()
    ) }


    viewModel {
        ReadMailViewModel(
            getMails = get(),
            updateMailMetadata = get(),
            updateDBMailMetadata = get(),
            deleteMailFromServer = get(),
            deleteMailFromDB = get()
        )
    }

    viewModel { NewMailViewModel(sendMail = get()) }

    viewModel { BaseMailListViewModel(getMailHeadersFromDB = get(),
        getNewMailHeaders = get(),
        getMailHeadersAfterIdFromDB = get())
    }
    viewModel {InboxViewModel(getMailHeadersFromDBUseCase = get(),
        getNewMailHeadersUseCase = get(),
        getMailHeadersAfterIdFromDB = get())
    }
    viewModel {SendViewModel(getMailHeadersFromDBUseCase = get(),
        getNewMailHeadersUseCase = get(),
        getMailHeadersAfterIdFromDB = get())
    }
    viewModel {CorporationViewModel(getMailHeadersFromDBUseCase = get(),
        getNewMailHeadersUseCase = get(),
        getMailHeadersAfterIdFromDB = get())
    }
    viewModel {AllianceViewModel(getMailHeadersFromDB = get(),
        getNewMailHeaders = get(),
        getMailHeadersAfterIdFromDB = get())
    }
    viewModel {MailingListViewModel(getMailHeadersFromDBUseCase = get(),
        getNewMailHeadersUseCase = get(),
        getMailHeadersAfterIdFromDB = get())
    }
}