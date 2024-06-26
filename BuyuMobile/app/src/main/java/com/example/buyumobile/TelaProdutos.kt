package com.example.buyumobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.buyumobile.model.Loja
import com.example.buyumobile.model.MyGlobals
import com.example.buyumobile.model.Produtos
import com.example.buyumobile.network.RetrofitService
import com.example.buyumobile.ui.theme.BuyuMobileTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TelaProdutos : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.extras

        setContent {
            BuyuMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TelaProdutos(extras)
                }
            }
        }
    }
}

@Composable
fun TelaProdutos(extras: Bundle?, modifier: Modifier = Modifier) {

    var lojaIdMock = extras?.getString("idLoja")

    if (lojaIdMock == null) {
        lojaIdMock = "9dbdc138-98d2-4643-8e7d-7ea2a771bcf5"
    }

    // TODO ISSO DAQ TIRAR
    lojaIdMock = "9dbdc138-98d2-4643-8e7d-7ea2a771bcf5"

    val erroApi = remember {
        mutableStateOf("")
    }

    val apiLojas = RetrofitService.getApiLojas()
    // TODO verificar depois
    val listaProdutos = remember { mutableStateListOf<Produtos>() }
    val listaProdutosVazio = remember { mutableStateListOf<Produtos>() }
    val loja = remember { mutableStateOf<Loja?>(null) }
    val get = apiLojas.getLoja(lojaIdMock)

    LaunchedEffect(key1 = true){
        get.enqueue(object : Callback<Loja> {
            override fun onResponse(call: Call<Loja>, response: Response<Loja>){
                if(response.isSuccessful){
                    loja.value = response.body()
                    if (loja != null){
                        listaProdutos.clear()
                        listaProdutos.addAll(loja.value?.produtos ?: listaProdutosVazio)
                    }
                }
            }
            override fun onFailure(call: Call<Loja>, t: Throwable) {
                erroApi.value = t.message!!
            }
        })
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp), // espaço para o footer
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${loja.value?.nome}",
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 24.sp,
                color = Color.White
            )

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            ) {
                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(listaProdutos) { produto ->
                        ProductBlock(
                            productName = produto.nome,
                            productDescription = produto.descricao,
                            productPrice = produto.valorUnitario.toString(),
                            imageResource = produto.imagens[0].nomeArquivoSalvo,
                            idProduto = produto.id.toString(),
                            idLoja = loja.value?.id.toString(),
                            descricaoProduto = produto.descricao,
                            nomeProduto = produto.nome,
                            imagemProduto = produto.imagens[0].nomeArquivoSalvo,
                            precoProduto = produto.valorUnitario
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            MenuFooter()
        }
    }
}




@Composable
fun MenuItem(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(8.dp)
    )
}


@Composable
fun ProductBlock(productName: String, productDescription: String, productPrice: String, imageResource: String,
                 idProduto: String, idLoja: String, precoProduto: Double, descricaoProduto: String, nomeProduto: String, imagemProduto: String) {
    // TODO passar o id do produto
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .clickable {
                // TODO chamar TelaDescProduto
                val intent = Intent(context, TelaDescProduto::class.java)
                intent.putExtra("idLoja", idLoja)
                intent.putExtra("idProduto", idProduto)
                intent.putExtra("precoProduto", precoProduto)
                intent.putExtra("descricaoProduto", descricaoProduto)
                intent.putExtra("nomeProduto", nomeProduto)
                intent.putExtra("imagemProduto", imagemProduto)
                context.startActivity(intent)
            }
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White)
            .padding(top = 1.dp, bottom = 1.dp)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Bloco de informações do produto
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = productName,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = productDescription,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "R$ $productPrice",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Imagem do produto
        // TODO arrumar imagem
        AsyncImage(
            model = "http://${MyGlobals.ipFixo}:8080/api/midias/imagens/${imageResource}",
            contentDescription = "Logo do Produto",
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .clickable(
                    onClick = {
//                        val intent = Intent(context, TelaProdutos::class.java)
//                        context.startActivity(intent)
                    }
                )
        )
    }
}

@Composable
fun MenuFooter() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                val intent = Intent(context, TelaPagamento::class.java)
                context.startActivity(intent)
            }) {
                Text("Carrinho")
            }
            Button(onClick = {
                val intent = Intent(context, TelaUltimosPedidos::class.java)
                context.startActivity(intent)
            }) {
                Text("Pedidos")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview6() {
    BuyuMobileTheme {
        TelaProdutos(null)
    }
}