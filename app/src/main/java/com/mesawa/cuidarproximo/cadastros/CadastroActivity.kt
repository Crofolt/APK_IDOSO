package com.mesawa.cuidarproximo.cadastros

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mesawa.cuidarproximo.R
import com.mesawa.cuidarproximo.ui.home.HomeActivity

class CadastroActivity : AppCompatActivity() {

    private lateinit var cadastroViewModel: CadastroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        supportActionBar?.hide()

        cadastroViewModel = ViewModelProvider(this)[CadastroViewModel::class.java]

        // Observar o status do cadastro
        cadastroViewModel.cadastroStatus.observe(this, Observer { status ->
            when (status) {
                "sucesso" -> {
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT)
                        .show()

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                "erro" -> {
                    Toast.makeText(this, "Erro no cadastro. Tente novamente.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        // 🔥 AGORA COMEÇA COM A PRIMEIRA TELA (Conta)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CadastroContaFragment())
                .commit()
        }
    }

    // 🔥 FUNÇÃO PRA TROCAR FRAGMENT (ESSENCIAL)
    fun navegarPara(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}