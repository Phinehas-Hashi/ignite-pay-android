package com.sparkstack.ignitepay.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class Transaction(val title:String,val subtitle:String,val amount:String,val incoming:Boolean)
private val demoTransactions = listOf(
    Transaction("Demo collection","Today · Test mode","+ KES 12,500",true),
    Transaction("Demo transfer","Yesterday · Test mode","− KES 3,200",false),
    Transaction("Demo payment","28 Sep · Test mode","− KES 1,850",false)
)

@Composable
fun IgnitePayApp() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(
        topBar={ TopAppBar(title={Text("Ignite Pay",fontWeight=FontWeight.Bold)},actions={
            IconButton(onClick={}) { Icon(Icons.Outlined.Security,"Security") }
        }) },
        bottomBar={
            NavigationBar {
                val items=listOf("Home" to Icons.Outlined.Home,"Transactions" to Icons.Outlined.ReceiptLong,"Wallet" to Icons.Outlined.AccountBalanceWallet,"Profile" to Icons.Outlined.Person)
                items.forEachIndexed { i,(label,icon) ->
                    NavigationBarItem(selected=selectedTab==i,onClick={selectedTab=i},icon={Icon(icon,label)},label={Text(label)})
                }
            }
        }
    ){ p ->
        when(selectedTab){
            0->HomeScreen(Modifier.padding(p))
            1->TransactionsScreen(Modifier.padding(p))
            2->WalletScreen(Modifier.padding(p))
            else->ProfileScreen(Modifier.padding(p))
        }
    }
}

@Composable private fun HomeScreen(modifier:Modifier=Modifier){
    LazyColumn(modifier.fillMaxSize().padding(horizontal=20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)){
        item{Spacer(Modifier.height(8.dp))}
        item{
            Card(shape=RoundedCornerShape(24.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primary)){
                Column(Modifier.padding(22.dp)){
                    Text("Available balance",color=MaterialTheme.colorScheme.onPrimary.copy(alpha=.75f))
                    Text("KES 0.00",fontSize=34.sp,fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.onPrimary)
                    Spacer(Modifier.height(8.dp))
                    Text("TEST MODE · NO REAL FUNDS",fontSize=12.sp,fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
        item{
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)){
                QuickAction("Send",Icons.Outlined.ArrowUpward,Modifier.weight(1f))
                QuickAction("Request",Icons.Outlined.ArrowDownward,Modifier.weight(1f))
                QuickAction("Wallet",Icons.Outlined.AccountBalanceWallet,Modifier.weight(1f))
            }
        }
        item{Text("Recent activity",fontSize=20.sp,fontWeight=FontWeight.Bold)}
        items(demoTransactions){TransactionRow(it)}
    }
}

@Composable private fun QuickAction(label:String,icon:androidx.compose.ui.graphics.vector.ImageVector,modifier:Modifier){
    OutlinedButton(onClick={},modifier=modifier.height(72.dp),shape=RoundedCornerShape(18.dp)){
        Column(horizontalAlignment=Alignment.CenterHorizontally){Icon(icon,null,Modifier.size(20.dp));Spacer(Modifier.height(4.dp));Text(label,fontSize=12.sp)}
    }
}

@Composable private fun TransactionsScreen(modifier:Modifier=Modifier){
    LazyColumn(modifier.fillMaxSize().padding(horizontal=20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
        item{Spacer(Modifier.height(12.dp));Text("Transactions",fontSize=28.sp,fontWeight=FontWeight.Bold);Text("Your payment history will appear here.");Spacer(Modifier.height(8.dp))}
        items(demoTransactions){TransactionRow(it)}
    }
}

@Composable private fun WalletScreen(modifier:Modifier=Modifier){
    Column(modifier.fillMaxSize().padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)){
        Text("Wallet",fontSize=28.sp,fontWeight=FontWeight.Bold)
        Text("Manage your Ignite Pay balance and funding methods.")
        Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(20.dp)){
            Column(Modifier.padding(20.dp)){
                Text("Current balance");Text("KES 0.00",fontSize=30.sp,fontWeight=FontWeight.Bold)
                Spacer(Modifier.height(12.dp));Button(onClick={},Modifier.fillMaxWidth()){Text("Add money")}
            }
        }
    }
}

@Composable private fun ProfileScreen(modifier:Modifier=Modifier){
    Column(modifier.fillMaxSize().padding(20.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){
        Text("Profile & security",fontSize=28.sp,fontWeight=FontWeight.Bold)
        Text("Account identity, PIN, devices and security activity will live here.")
        OutlinedButton(onClick={},Modifier.fillMaxWidth()){Text("Security settings")}
        OutlinedButton(onClick={},Modifier.fillMaxWidth()){Text("Account settings")}
    }
}

@Composable private fun TransactionRow(t:Transaction){
    Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(18.dp)){
        Row(Modifier.padding(16.dp),verticalAlignment=Alignment.CenterVertically){
            Icon(if(t.incoming) Icons.Outlined.ArrowDownward else Icons.Outlined.ArrowUpward,null)
            Column(Modifier.weight(1f).padding(horizontal=12.dp)){Text(t.title,fontWeight=FontWeight.SemiBold);Text(t.subtitle,fontSize=12.sp)}
            Text(t.amount,fontWeight=FontWeight.Bold)
        }
    }
}
