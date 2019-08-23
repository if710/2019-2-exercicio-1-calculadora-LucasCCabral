package br.ufpe.cin.android.calculadora

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import java.util.*

class MainActivity : AppCompatActivity() {

    // variaveis que sao mais usadas
    // vao ser declaradas aqui (facilidade do escopo)
    private var expr  : String = ""
    private lateinit var exprView : EditText
    private lateinit var result : TextView
    private lateinit var row1 : LinearLayout
    private lateinit var row2 : LinearLayout
    private lateinit var row3 : LinearLayout
    private lateinit var row4 : LinearLayout
    private lateinit var row5 : LinearLayout
    private lateinit var model : MyViewModel

    // Valor descoberto atraves de experimentacao
    // nao achei um valor dado pelo android que
    // correspondesse.
    private val LANDSCAPE : Int = 2

    // View model tem um lifecycle diferente de uma activity
    // ela continua viva apos a morte de uma activity e pode
    // ser usada para fazer a passagem de parametros para outra
    // atividade. Essa foi uma solucao sugerida pela documentacao
    // do android studio.
    // source: https://developer.android.com/topic/libraries/architecture/saving-states.html
    class MyViewModel : ViewModel() {
        private var exprView : String = ""
        private var result : String = ""

        fun setExprView(str: String) {exprView = str}
        fun getExprView() : String {return exprView}

        fun setResult(str: String) {result = str}
        fun getResult() : String {return result}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //View model para poder salvar os dados apos mudança de orientacao
        model = ViewModelProviders.of(this)[MyViewModel::class.java]
        exprView = findViewById(R.id.text_calc)
        result = findViewById(R.id.text_info)
        // Gives me the ability to control each row
        row1 = findViewById(R.id.row_1)
        row2 = findViewById(R.id.row_2)
        row3 = findViewById(R.id.row_3)
        row4 = findViewById(R.id.row_4)
        row5 = findViewById(R.id.row_5)

        //get orientation object to discover the orientation
        val orientation = getResources().getConfiguration().orientation;
        // checks if the variable is in landscape mode
        if(orientation == LANDSCAPE){
            hideAllButtons()
        }

        expr = model.getExprView()
        exprView.setText(model.getExprView())
        result.text = model.getResult()

        //declaraçao e inicializacao dos botoes
        val button0 = findViewById<Button>(R.id.btn_0)
        val button1 = findViewById<Button>(R.id.btn_1)
        val button2 = findViewById<Button>(R.id.btn_2)
        val button3 = findViewById<Button>(R.id.btn_3)
        val button4 = findViewById<Button>(R.id.btn_4)
        val button5 = findViewById<Button>(R.id.btn_5)
        val button6 = findViewById<Button>(R.id.btn_6)
        val button7 = findViewById<Button>(R.id.btn_7)
        val button8 = findViewById<Button>(R.id.btn_8)
        val button9 = findViewById<Button>(R.id.btn_9)
        val buttonDiv = findViewById<Button>(R.id.btn_Divide)
        val buttonMult = findViewById<Button>(R.id.btn_Multiply)
        val buttonAdd = findViewById<Button>(R.id.btn_Add)
        val buttonSub = findViewById<Button>(R.id.btn_Subtract)
        val buttonPower = findViewById<Button>(R.id.btn_Power)
        val buttonClear = findViewById<Button>(R.id.btn_Clear)
        val buttonDot= findViewById<Button>(R.id.btn_Dot)
        val buttonEqual= findViewById<Button>(R.id.btn_Equal)
        val buttonLParen= findViewById<Button>(R.id.btn_LParen)
        val buttonRparen= findViewById<Button>(R.id.btn_RParen)

        //setando listener para clique dos respectivos botoes
        button0.setOnClickListener{
            addToExpression('0')
        }
        button1.setOnClickListener{
            addToExpression('1')
        }
        button2.setOnClickListener{
            addToExpression('2')
        }
        button3.setOnClickListener{
            addToExpression('3')
        }
        button4.setOnClickListener{
            addToExpression('4')
        }
        button5.setOnClickListener{
            addToExpression('5')
        }
        button6.setOnClickListener{
            addToExpression('6')
        }
        button7.setOnClickListener{
            addToExpression('7')
        }
        button8.setOnClickListener{
            addToExpression('8')
        }
        button9.setOnClickListener{
            addToExpression('9')
        }
        buttonDiv.setOnClickListener{
            addToExpression('/')
        }
        buttonMult.setOnClickListener{
            addToExpression('*')
        }
        buttonAdd.setOnClickListener{
            addToExpression('+')
        }
        buttonSub.setOnClickListener{
            addToExpression('-')
        }
        buttonPower.setOnClickListener{
            addToExpression('^')
        }
        buttonDot.setOnClickListener{
            addToExpression('.')
        }
        buttonLParen.setOnClickListener{
            addToExpression('(')
        }
        buttonRparen.setOnClickListener{
            addToExpression(')')
        }
        buttonClear.setOnClickListener{
            clearExpression()
        }
        buttonEqual.setOnClickListener{
            //calculate expression
            var str : String = eval(expr).toString()
            result.text = str
            //Saves model result value
            model.setResult(expr)
        }
    }

    // Deixa todos as fileiras invisiveis
    fun hideAllButtons(){
        row1.visibility = View.INVISIBLE
        row2.visibility = View.INVISIBLE
        row3.visibility = View.INVISIBLE
        row4.visibility = View.INVISIBLE
        row5.visibility = View.INVISIBLE
    }

    // Adiciona uma variavel para a string e chama
    // update view para fazer o update da TextView da expressao
    fun addToExpression(char: Char){
        expr += char
        Log.d("DEV-LOG", expr);
        updateTextView()
    }

    // Zera a expressao e faz o update da TextView da expressao
    fun clearExpression(){
        expr = ""
        updateTextView()
    }

    // Era para deletar o ultimo argumento da expressao
    fun deleteArgument(){
        expr = expr.dropLast(1)
        Log.d("DEV-LOG", expr);
        updateTextView()
    }

    // Faz o update da TextView com a string em expr
    fun updateTextView(){
        exprView.setText(expr)
        // saves ViewModel text
        model.setExprView(expr)
    }

    // Funcao que gera um Toast para o usuario
    fun somethinWentWrong() : Double{
        Toast.makeText(applicationContext, "Algo de errado com a sua expressao, tente novamente.",
            Toast.LENGTH_LONG).show()
        return Double.NaN
    }

    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if(x == Double.NaN) return Double.NaN
                //so entra aqui se nao fizer o parsing completo da string
                if (pos < str.length) somethinWentWrong()
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                if(x == Double.NaN) return Double.NaN
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                if(x == Double.NaN) return Double.NaN
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        //Nao preciso modificar aqui pois essa parte do codigo nunca vai rodar(com essa calculadora)
                        //throw RuntimeException("Função desconhecida: " + func)
                        somethinWentWrong()
                } else {
                    //Entra aqui quando for detectado um erro na expressao
                    return somethinWentWrong()
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
