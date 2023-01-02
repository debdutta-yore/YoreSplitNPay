package co.yore.splitnpay.kontakts

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract

class Kontakts(private val block: Kontakts.()->Unit){

    infix fun String.equals(value: Any): Expression{
        return Expression(this, Expression.Operator.Equals,value)
    }
    infix fun String.like(value: Any): Expression{
        return Expression(this, Expression.Operator.Like,"%$value%")
    }
    infix fun String.likePost(value: Any): Expression{
        return Expression(this, Expression.Operator.Like,"%$value")
    }
    infix fun String.likePre(value: Any): Expression{
        return Expression(this, Expression.Operator.Like,"$value%")
    }
    infix fun String.gt(value: Any): Expression{
        return Expression(this, Expression.Operator.Gt,value)
    }
    infix fun String.lt(value: Any): Kontakts.Expression{
        return Expression(this, Expression.Operator.Lt,value)
    }
    infix fun String.gte(value: Any): Expression{
        return Expression(this, Expression.Operator.Gte,value)
    }
    infix fun String.lte(value: Any): Kontakts.Expression{
        return Expression(this, Expression.Operator.Lte,value)
    }
    infix fun Expression.and(right: Expression): Expression{
        return Expression(this, Expression.Operator.And,right)
    }
    infix fun Expression.or(right: Expression): Expression{
        return Expression(this, Expression.Operator.Or,right)
    }

    val Cursor?.columns get(): List<String> = this?.columnNames?.toList()?: emptyList<String>()
    val Cursor?.count get() = this?.count?:0
    fun Cursor?.forEach(block: Cursor.(Int)->Unit): Cursor?{
        var index = -1
        this?.let{
            while(it.moveToNext()){
                block(it, ++index)
            }
        }
        return this
    }

    val String.asc get() = "$this ASC"
    val String.desc get() = "$this DESC"
    val String.pre get() = "%$this"
    val String.post get() = "$this%"
    val String.prePost get() = "%$this%"

    init {
        block()
    }
    class Expression(
        val left: Any,
        val operator: Operator,
        val right: Any
    ){
        sealed class Operator(val value: String){
            object Equals: Operator("=")
            object Like: Operator("LIKE")
            object Gt: Operator(">")
            object Lt: Operator("lt")
            object Gte: Operator(">=")
            object Lte: Operator("<=")
            object And: Operator("AND")
            object Or: Operator("OR")
        }

        override fun toString(): String {
            val _left = if(left !is Expression) left.toString() else "(${left.left} ${left.operator.value} ${left.right})"
            val _right = if(right !is Expression) "'$right'" else "(${right.left} ${right.operator.value} ${right.left})"
            return "$_left ${operator.value} $_right"
        }
    }
    class Query(
        val cr: ContentResolver,
        val from: Uri,
        val projection: List<String>? = null,
        val selection: String? = null,
        val selectionArgs: List<String>? = null,
        val sortOrder: String? = null
    ){
        fun toCursor(): Cursor?{
            return cr.query(
                from,
                projection?.toTypedArray(),

                selection,
                selectionArgs?.toTypedArray(),
                sortOrder
            )
        }
    }
    class Contents{
        private lateinit var _from: Uri
        private var _fields = mutableListOf<String>()
        private var _selections = mutableListOf<Expression>()
        private var rawSelection:Pair<String,List<String>>? = null
        private var sorts = mutableListOf<String>()
        fun from(uri: Uri): Contents{
            _from = uri
            return this
        }
        fun select(vararg fields: String): Contents{
            _fields.addAll(fields)
            return this
        }
        fun select(fields: List<String>): Contents{
            _fields.addAll(fields)
            return this
        }
        fun where(vararg expression: Expression): Contents{
            _selections.addAll(expression)
            return this
        }
        fun where(expressions: List<Expression>): Contents{
            _selections.addAll(expressions)
            return this
        }
        fun where(
            whereClause: String,
            args: List<String> = emptyList()
        ): Contents{
            rawSelection = Pair(whereClause, args)
            return this
        }
        fun sortBy(vararg fieldDir: String): Contents{
            sorts.addAll(fieldDir)
            return this
        }

        fun sortBy(fieldDirs: List<String>): Contents{
            sorts.addAll(fieldDirs)
            return this
        }
        fun build(cr: ContentResolver): Cursor?{
            var sel: List<String>
            var args = mutableListOf<String>()
            if(rawSelection != null){
                sel = listOf(
                    rawSelection!!.first
                )
                args = rawSelection!!.second.toMutableList()
            }
            else{
                sel = _selections.map { it.toString() }
            }
            return Query(
                cr = cr,
                from = _from,
                projection = if(_fields.isEmpty()) null else _fields,
                selection = if(sel.isEmpty()) null else sel.joinToString(" AND "),
                selectionArgs = if(args.isEmpty()) null else args,
                sortOrder = if(sorts.isEmpty()) null else sorts.joinToString(", ")
            ).toCursor()
        }
    }
    companion object{
        val Contacts = ContactsContract.Contacts.CONTENT_URI
        val Phone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val Email = ContactsContract.CommonDataKinds.Email.CONTENT_URI
    }

    fun from(uri: Uri): Contents{
        return Contents().from(uri)
    }
}