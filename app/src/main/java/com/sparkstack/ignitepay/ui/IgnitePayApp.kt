package com.sparkstack.ignitepay.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private enum class AppStage { Welcome, Login, SignUp, Home }

private data class Transaction(
    val title: String,
    val subtitle: String,
    val amount: String,
    val incoming: Boolean,
    val status: String = "Completed"
)

private val demoTransactions = listOf(
    Transaction("Demo collection", "Today · Test mode", "+ KES 12,500", true),
    Transaction("Demo transfer", "Yesterday · Test mode", "− KES 3,200", false),
    Transaction("Demo payment", "28 Sep · Test mode", "− KES 1,850", false)
)

@Composable
fun IgnitePayApp() {
    var stage by rememberSaveable { mutableStateOf(AppStage.Welcome) }

    when (stage) {
        AppStage.Welcome -> WelcomeScreen(
            onGetStarted = { stage = AppStage.SignUp },
            onLogin = { stage = AppStage.Login }
        )
        AppStage.Login -> AuthScreen(
            title = "Welcome back",
            subtitle = "Sign in to your Ignite Pay account.",
            primaryLabel = "Sign in",
            secondaryLabel = "Create an account",
            onPrimary = { stage = AppStage.Home },
            onSecondary = { stage = AppStage.SignUp }
        )
        AppStage.SignUp -> AuthScreen(
            title = "Create your account",
            subtitle = "Set up your Ignite Pay identity in a few steps.",
            primaryLabel = "Continue",
            secondaryLabel = "I already have an account",
            onPrimary = { stage = AppStage.Home },
            onSecondary = { stage = AppStage.Login }
        )
        AppStage.Home -> MainShell(onSignOut = { stage = AppStage.Welcome })
    }
}

@Composable
private fun WelcomeScreen(onGetStarted: () -> Unit, onLogin: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        Color(0xFF10131A),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BrandMark()
                TextButton(onClick = onLogin) { Text("Sign in") }
            }

            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                ) {
                    Text(
                        "PAYMENTS, SIMPLIFIED.",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "Move money.
Build opportunity.",
                    fontSize = 42.sp,
                    lineHeight = 46.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Ignite Pay is being built as a secure financial layer for people and businesses — simple enough for everyday payments, powerful enough to grow with you.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f)
                    ),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Row(
                        Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.VerifiedUser,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Column(Modifier.padding(start = 14.dp)) {
                            Text("Security-first by design", fontWeight = FontWeight.Bold)
                            Text(
                                "Real accounts and real funds come later. This build is test mode only.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
                            )
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Get started", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Outlined.ArrowForward, null)
                }
                Text(
                    "Ignite Pay · Frontend preview 0.2.0",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.42f),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun AuthScreen(
    title: String,
    subtitle: String,
    primaryLabel: String,
    secondaryLabel: String,
    onPrimary: () -> Unit,
    onSecondary: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BrandMark()
            IconButton(onClick = onSecondary) {
                Icon(Icons.Outlined.Close, "Back")
            }
        }

        Spacer(Modifier.height(18.dp))
        Text(title, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
        Text(
            subtitle,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Phone or email") },
            leadingIcon = { Icon(Icons.Outlined.Person, null) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Outlined.Lock, null) },
            trailingIcon = { Icon(Icons.Outlined.Visibility, "Show password") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )

        if (primaryLabel == "Continue") {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Full name") },
                leadingIcon = { Icon(Icons.Outlined.Badge, null) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
        }

        Spacer(Modifier.height(4.dp))
        Button(
            onClick = onPrimary,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(primaryLabel, fontWeight = FontWeight.Bold)
        }
        TextButton(onClick = onSecondary, modifier = Modifier.fillMaxWidth()) {
            Text(secondaryLabel)
        }
        Spacer(Modifier.weight(1f))
        Text(
            "TEST MODE · Authentication is a frontend preview and does not create a real financial account.",
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
            fontSize = 11.sp,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun MainShell(onSignOut: () -> Unit) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Ignite Pay", fontWeight = FontWeight.ExtraBold)
                        Text(
                            "TEST MODE",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.NotificationsNone, "Notifications")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Security, "Security")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    "Home" to Icons.Outlined.Home,
                    "Transactions" to Icons.Outlined.ReceiptLong,
                    "Wallet" to Icons.Outlined.AccountBalanceWallet,
                    "Profile" to Icons.Outlined.Person
                )
                items.forEachIndexed { i, (label, icon) ->
                    NavigationBarItem(
                        selected = selectedTab == i,
                        onClick = { selectedTab = i },
                        icon = { Icon(icon, label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> HomeScreen(Modifier.padding(padding))
            1 -> TransactionsScreen(Modifier.padding(padding))
            2 -> WalletScreen(Modifier.padding(padding))
            else -> ProfileScreen(Modifier.padding(padding), onSignOut)
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier.fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(Modifier.height(8.dp)) }
        item {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(Modifier.padding(22.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Available balance",
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.72f)
                        )
                        Icon(
                            Icons.Outlined.AccountBalanceWallet,
                            null,
                            tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "KES 0.00",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.height(12.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            "TEST MODE · NO REAL FUNDS",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
        item {
            Text("Quick actions", fontSize = 19.sp, fontWeight = FontWeight.Bold)
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickAction("Send", Icons.Outlined.ArrowUpward, Modifier.weight(1f))
                QuickAction("Request", Icons.Outlined.ArrowDownward, Modifier.weight(1f))
                QuickAction("Pay", Icons.Outlined.QrCode2, Modifier.weight(1f))
                QuickAction("More", Icons.Outlined.MoreHoriz, Modifier.weight(1f))
            }
        }
        item {
            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.11f)
                )
            ) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.AutoGraph, null, tint = MaterialTheme.colorScheme.secondary)
                        }
                    }
                    Column(Modifier.padding(start = 14.dp)) {
                        Text("Your financial layer is coming", fontWeight = FontWeight.Bold)
                        Text(
                            "Payments, collections, payouts and business tools will connect here.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
                        )
                    }
                }
            }
        }
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent activity", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                TextButton(onClick = {}) { Text("See all") }
            }
        }
        items(demoTransactions) { TransactionRow(it) }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun QuickAction(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier
) {
    OutlinedButton(
        onClick = {},
        modifier = modifier.height(82.dp),
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, Modifier.size(20.dp))
            Spacer(Modifier.height(5.dp))
            Text(label, fontSize = 11.sp)
        }
    }
}

@Composable
private fun TransactionsScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier.fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(Modifier.height(12.dp))
            Text("Transactions", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text(
                "A clear ledger for every payment, transfer and payout.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
            )
            Spacer(Modifier.height(8.dp))
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(true, "All")
                FilterChip(false, "Completed")
                FilterChip(false, "Pending")
            }
        }
        items(demoTransactions) { TransactionRow(it) }
    }
}

@Composable
private fun FilterChip(selected: Boolean, label: String) {
    AssistChip(
        onClick = {},
        label = { Text(label) },
        leadingIcon = if (selected) {
            { Icon(Icons.Outlined.Check, null, Modifier.size(16.dp)) }
        } else null,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            else MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

@Composable
private fun WalletScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Wallet", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text(
                "Your money, funding methods and future ledger tools in one place.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
            )
        }
        item {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.padding(20.dp)) {
                    Text("Current balance", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f))
                    Text("KES 0.00", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(14.dp))
                    Button(onClick = {}, Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                        Icon(Icons.Outlined.Add, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Add money")
                    }
                }
            }
        }
        item { WalletOption("Withdraw", Icons.Outlined.ArrowDownward) }
        item { WalletOption("Funding methods", Icons.Outlined.CreditCard) }
        item { WalletOption("Ledger", Icons.Outlined.AccountBalance) }
    }
}

@Composable
private fun WalletOption(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    OutlinedButton(
        onClick = {},
        modifier = Modifier.fillMaxWidth().height(58.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Icon(icon, null)
        Text(label, Modifier.weight(1f).padding(start = 12.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Start)
        Icon(Icons.Outlined.ChevronRight, null)
    }
}

@Composable
private fun ProfileScreen(modifier: Modifier = Modifier, onSignOut: () -> Unit) {
    LazyColumn(
        modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Profile & security", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text(
                "Identity, PIN, biometrics, devices and security activity.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
            )
        }
        item { ProfileOption("Personal details", Icons.Outlined.Badge) }
        item { ProfileOption("PIN & biometrics", Icons.Outlined.Fingerprint) }
        item { ProfileOption("Trusted devices", Icons.Outlined.Devices) }
        item { ProfileOption("Security activity", Icons.Outlined.Security) }
        item { ProfileOption("Notifications", Icons.Outlined.NotificationsNone) }
        item {
            OutlinedButton(
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Outlined.Logout, null)
                Spacer(Modifier.width(8.dp))
                Text("Sign out of test session")
            }
        }
    }
}

@Composable
private fun ProfileOption(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    OutlinedButton(
        onClick = {},
        modifier = Modifier.fillMaxWidth().height(58.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Icon(icon, null)
        Text(
            label,
            Modifier.weight(1f).padding(start = 12.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Start
        )
        Icon(Icons.Outlined.ChevronRight, null)
    }
}

@Composable
private fun TransactionRow(t: Transaction) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                color = if (t.incoming) Color(0xFF173F32) else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        if (t.incoming) Icons.Outlined.ArrowDownward else Icons.Outlined.ArrowUpward,
                        null,
                        tint = if (t.incoming) Color(0xFF65D6A0) else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(t.title, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(
                    t.subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.56f)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(t.amount, fontWeight = FontWeight.Bold)
                Text(
                    t.status,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun BrandMark() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(38.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Outlined.Bolt,
                    contentDescription = "Ignite Pay",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Text("Ignite Pay", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
    }
}
