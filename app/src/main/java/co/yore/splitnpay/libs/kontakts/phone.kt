package co.yore.splitnpay.libs.kontakts

fun phone(value: String): String?{
    if(value.isEmpty()){
        return null
    }
    var r = value
    r = r.filter { it == '+' || it.isDigit() }
    val plusCount = r.count { it == '+' }
    if(plusCount > 1){
        return null
    }
    if(plusCount == 1 && !r.startsWith('+')){
        return null
    }
    if(r.startsWith("+91") && r.length == 13){
        return r
    }
    if(r.startsWith("0") && r.length == 11){
        r = r.drop(1)
        return "+91$r"
    }
    if(r.startsWith("91") && r.length == 12){
        return "+$r"
    }
    if(r.length == 10){
        return "+91$r"
    }
    return null
}