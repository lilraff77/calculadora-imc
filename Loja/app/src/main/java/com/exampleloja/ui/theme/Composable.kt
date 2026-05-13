package com.example.loja.ui.screens

// 🔹 Imports
import android.graphics.BitmapFactory
import java.io.InputStream

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

import com.example.loja.Produto

// 🔹 NAVIGATION + ESTADO DO CARRINHO
@Composable
fun LojaApp() {

    val navController = rememberNavController()
    val carrinho = remember { mutableStateListOf<Produto>() }

    NavHost(
        navController = navController,
        startDestination = "lista"
    ) {

        // 🟢 Lista
        composable("lista") {
            ListaProdutos(
                onProdutoClick = { produto ->
                    navController.navigate(
                        "detalhes/${produto.nome}/${produto.preco}/${produto.imagemAsset}"
                    )
                },
                onCarrinhoClick = {
                    navController.navigate("carrinho")
                }
            )
        }

        // 🟣 Detalhes
        composable(
            route = "detalhes/{nome}/{preco}/{imagemAsset}",
            arguments = listOf(
                navArgument("nome") { type = NavType.StringType },
                navArgument("preco") { type = NavType.StringType },
                navArgument("imagemAsset") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val nome = backStackEntry.arguments?.getString("nome") ?: ""
            val preco = backStackEntry.arguments?.getString("preco") ?: ""
            val imagemAsset = backStackEntry.arguments?.getString("imagemAsset") ?: ""

            ProdutoDetalhes(
                nome = nome,
                preco = preco,
                imagemAsset = imagemAsset,
                onAddCarrinho = { produto ->
                    carrinho.add(produto)
                    navController.navigate("carrinho")
                }
            )
        }

        // 🛒 Carrinho
        composable("carrinho") {
            CarrinhoScreen(carrinho)
        }
    }
}

// 🔹 LISTA
@Composable
fun ListaProdutos(
    onProdutoClick: (Produto) -> Unit,
    onCarrinhoClick: () -> Unit
) {

    val produtos = listOf(
        Produto("Camiseta Azul", "R$ 49,90", "camiseta_azul.png"),
        Produto("Boné Preto", "R$ 29,90", "bone_preto.jpg"),
        Produto("Caneca Personalizada", "R$ 39,90", "caneca.png")
    )

    Column {

        Button(
            onClick = onCarrinhoClick,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Ver Carrinho 🛒")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(produtos) { produto ->
                ProdutoItem(produto) {
                    onProdutoClick(produto)
                }
            }
        }
    }
}

// 🔹 ITEM
@Composable
fun ProdutoItem(
    produto: Produto,
    onClick: () -> Unit
) {

    val context = LocalContext.current

    val imageBitmap: ImageBitmap? = remember(produto.imagemAsset) {
        try {
            val inputStream: InputStream = context.assets.open(produto.imagemAsset)
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        imageBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = produto.nome,
                modifier = Modifier.size(80.dp)
            )
        }

        Column {
            Text(produto.nome, style = MaterialTheme.typography.titleMedium)
            Text(produto.preco, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// 🔹 DETALHES
@Composable
fun ProdutoDetalhes(
    nome: String,
    preco: String,
    imagemAsset: String,
    onAddCarrinho: (Produto) -> Unit
) {

    val context = LocalContext.current

    val imageBitmap: ImageBitmap? = remember(imagemAsset) {
        try {
            val inputStream: InputStream = context.assets.open(imagemAsset)
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        imageBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = nome,
                modifier = Modifier.size(200.dp)
            )
        }

        Text(nome, style = MaterialTheme.typography.headlineMedium)
        Text(preco, style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onAddCarrinho(Produto(nome, preco, imagemAsset))
            }
        ) {
            Text("Adicionar ao Carrinho")
        }
    }
}

// 🔹 CARRINHO
@Composable
fun CarrinhoScreen(carrinho: List<Produto>) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("🛒 Seu Carrinho", style = MaterialTheme.typography.headlineMedium)

        if (carrinho.isEmpty()) {
            Text("Carrinho vazio")
        } else {
            LazyColumn {
                items(carrinho) { produto ->
                    ProdutoItem(produto, onClick = {})
                }
            }
        }
    }
}