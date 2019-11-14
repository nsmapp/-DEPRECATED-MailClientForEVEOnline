package by.nepravskysm.domain.usecase.base


import kotlinx.coroutines.*

typealias CompliteCase<T> = AsyncUseCase.Request<T>.() -> Unit

abstract class AsyncUseCase<T> {

    private var job : Job = SupervisorJob()

    var backgaund = Dispatchers.IO
    var foreground = Dispatchers.Main

    protected abstract suspend fun onBackground():T

    fun execute(block :CompliteCase<T> ){

        var response = Request<T>().apply { block() }
        unsubscribe()

        job = SupervisorJob()
        CoroutineScope(foreground + job).launch {
            try {
                val result = withContext(backgaund){
                    onBackground()
                }
                response(result)
            }catch (cancelExeption :CancellationException){
                response(cancelExeption)
            }catch (e :Exception){
                response(e)
            }
        }




    }

    private fun unsubscribe() {
        job.apply {
            cancelChildren()
            cancel()
        }
    }


    class Request<T>{

        private var onComplete: ((T) -> Unit)? = null
        private var onError: ((Exception) -> Unit)? = null
        private var onCancel: ((CancellationException) -> Unit)? = null


        fun onComplite(block :(T) -> Unit){
            onComplete = block
        }

        fun onError(block :(Exception) -> Unit){
            onError = block
        }

        fun onCancel(block: (CancellationException) -> Unit) {
            onCancel = block
        }

        operator fun invoke(result :T) {
            onComplete?.let {
                it.invoke(result)
            }
        }

        operator fun invoke(error :Exception) {
            onError?.let {
                it.invoke(error)

            }
        }

        operator fun invoke(error :CancellationException) {
            onCancel?.let {
                it.invoke(error)
            }
        }

    }

}