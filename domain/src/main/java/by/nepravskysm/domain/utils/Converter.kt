package by.nepravskysm.domain.utils

fun stringIsLong(string :String) :Boolean{

    if (string.equals("")){
        return false
    }
    try {
        val d = string.toInt()
    }catch (e :NumberFormatException){
        return false
    }
    return true
}

fun stringToLong(string : String) :Long{

    if (stringIsLong(string)){
        return string.toLong()
    }
    return 0
}
