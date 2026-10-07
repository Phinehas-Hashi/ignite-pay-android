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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private enum class AppStage { Welcome, Login, SignUp, Home }

private enum class PaymentFlow { Send, Request, Pay }

private enum class PaymentStep { Recipient, Amount, Review, Processing, Result }

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
    Transaction("Demo payment", "28 Sep · Test mode", "− KES 1,850", false),
    Transaction("Pending payout", "Test mode · Processing", "− KES 5,000", false, "Pending")
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
                    "Move money.\nBuild opportunity.",
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
                    "Ignite Pay · Frontend preview 0.3.0",
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
    var activeFlow by rememberSaveable { mutableStateOf<PaymentFlow?>(null) }

    if (activeFlow != null) {
        PaymentFlowScreen(flow = activeFlow!!, onClose = { activeFlow = null })
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Ignite Pay", fontWeight = FontWeight.ExtraBold)
                        Text("TEST MODE", color = MaterialTheme.colorScheme.primary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Outlined.NotificationsNone, "Notifications") }
                    IconButton(onClick = {}) { Icon(Icons.Outlined.Security, "Security") }
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
            0 -> HomeScreen(
                Modifier.padding(padding),
                onSend = { activeFlow = PaymentFlow.Send },
                onRequest = { activeFlow = PaymentFlow.Request },
                onPay = { activeFlow = PaymentFlow.Pay },
                onSeeAll = { selectedTab = 1 }
            )
            1 -> TransactionsScreen(Modifier.padding(padding))
            2 -> WalletScreen(Modifier.padding(padding))
            else -> ProfileScreen(Modifier.padding(padding), onSignOut)
        }
    }
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    onSend: () -> Unit = {},
    onRequest: () -> Unit = {},
    onPay: () -> Unit = {},
    onSeeAll: () -> Unit = {}
) {
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
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Available balance", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.72f))
                        Icon(Icons.Outlined.AccountBalanceWallet, null, tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                    }
                    Spacer(Modifier.height(6.dp))
                    Text("KES 0.00", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimary)
                    Spacer(Modifier.height(12.dp))
                    Surface(color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f), shape = RoundedCornerShape(10.dp)) {
                        Text("TEST MODE · NO REAL FUNDS", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Quick actions", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Text("TEST MODE", color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickAction("Send", Icons.Outlined.ArrowUpward, Modifier.weight(1f), onSend)
                QuickAction("Request", Icons.Outlined.ArrowDownward, Modifier.weight(1f), onRequest)
                QuickAction("Pay", Icons.Outlined.QrCode2, Modifier.weight(1f), onPay)
                QuickAction("More", Icons.Outlined.MoreHoriz, Modifier.weight(1f), {})
            }
        }
        item {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.11f))) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(modifier = Modifier.size(44.dp), shape = CircleShape, color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f)) {
                        Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.AutoGraph, null, tint = MaterialTheme.colorScheme.secondary) }
                    }
                    Column(Modifier.padding(start = 14.dp)) {
                        Text("Your financial layer is coming", fontWeight = FontWeight.Bold)
                        Text("Payments, collections, payouts and business tools will connect here.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f))
                    }
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Recent activity", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                TextButton(onClick = onSeeAll) { Text("See all") }
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
    modifier: Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(onClick = onClick, modifier = modifier.height(82.dp), shape = RoundedCornerShape(18.dp), contentPadding = PaddingValues(4.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, Modifier.size(20.dp))
            Spacer(Modifier.height(5.dp))
            Text(label, fontSize = 11.sp)
        }
    }
}

@Composable
private fun TransactionsScreen(modifier: Modifier = Modifier) {
    var selectedFilter by rememberSaveable { mutableStateOf("All") }
    val filteredTransactions = when (selectedFilter) {
        "Completed" -> demoTransactions.filter { it.status == "Completed" }
        "Pending" -> demoTransactions.filter { it.status == "Pending" }
        else -> demoTransactions
    }

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
                listOf("All", "Completed", "Pending").forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        label = filter,
                        onClick = { selectedFilter = filter }
                    )
                }
            }
        }
        if (filteredTransactions.isEmpty()) {
            item {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                    Text(
                        "No $selectedFilter transactions in test mode.",
                        modifier = Modifier.padding(20.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
                    )
                }
            }
        } else {
            items(filteredTransactions) { TransactionRow(it) }
        }
    }
}

@Composable
private fun FilterChip(selected: Boolean, label: String, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
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
private fun PaymentFlowScreen(
    flow: PaymentFlow,
    onClose: () -> Unit
) {
    var step by rememberSaveable { mutableStateOf(PaymentStep.Recipient) }
    var recipient by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }

    val title = when (flow) {
        PaymentFlow.Send -> "Send money"
        PaymentFlow.Request -> "Request money"
        PaymentFlow.Pay -> "Pay"
    }
    val recipientLabel = when (flow) {
        PaymentFlow.Send -> "Recipient phone or email"
        PaymentFlow.Request -> "Request from phone or email"
        PaymentFlow.Pay -> "Merchant or payment reference"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.ExtraBold) },
                navigationIcon = { IconButton(onClick = onClose) { Icon(Icons.Outlined.ArrowBack, "Back") } },
                actions = { Text("TEST", color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 16.dp)) }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                when (step) {
                    PaymentStep.Recipient -> "Who are you paying?"
                    PaymentStep.Amount -> "How much?"
                    PaymentStep.Review -> "Review payment"
                    PaymentStep.Processing -> "Processing"
                    PaymentStep.Result -> if (flow == PaymentFlow.Request) "Request created" else "Payment complete"
                },
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                when (step) {
                    PaymentStep.Recipient -> "This is a frontend test flow. No real funds will move."
                    PaymentStep.Amount -> "Enter an amount for this test transaction."
                    PaymentStep.Review -> "Confirm the details before the simulated transaction runs."
                    PaymentStep.Processing -> "Ignite Pay is simulating the payment lifecycle."
                    PaymentStep.Result -> "This is a successful test result only."
                },
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
            )

            when (step) {
                PaymentStep.Recipient -> {
                    OutlinedTextField(
                        value = recipient,
                        onValueChange = { recipient = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(recipientLabel) },
                        leadingIcon = { Icon(Icons.Outlined.Person, null) },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = { step = PaymentStep.Amount },
                        enabled = recipient.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text("Continue", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Outlined.ArrowForward, null)
                    }
                }
                PaymentStep.Amount -> {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { if (it.all(Char::isDigit) && it.length <= 9) amount = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Amount in KES") },
                        leadingIcon = { Text("KES") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Note (optional)") },
                        leadingIcon = { Icon(Icons.Outlined.Notes, null) },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = { step = PaymentStep.Review },
                        enabled = amount.toLongOrNull()?.let { it > 0 } == true,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(18.dp)
                    ) { Text("Review", fontWeight = FontWeight.Bold) }
                }
                PaymentStep.Review -> {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp)) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            PaymentSummaryRow("Recipient", recipient)
                            PaymentSummaryRow("Amount", "KES " + amount.ifBlank { "0" })
                            if (note.isNotBlank()) PaymentSummaryRow("Note", note)
                            PaymentSummaryRow("Environment", "TEST MODE")
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = { step = PaymentStep.Processing },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Icon(Icons.Outlined.Lock, null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (flow == PaymentFlow.Request) "Create test request" else "Confirm test payment", fontWeight = FontWeight.Bold)
                    }
                }
                PaymentStep.Processing -> {
                    Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(18.dp))
                            Text("Securely processing…", fontWeight = FontWeight.Bold)
                            Text("No real transaction is being submitted.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f), fontSize = 12.sp)
                        }
                    }
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(900)
                        step = PaymentStep.Result
                    }
                }
                PaymentStep.Result -> {
                    Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(Modifier.size(72.dp), shape = CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Outlined.Check, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(38.dp))
                                }
                            }
                            Spacer(Modifier.height(18.dp))
                            Text(if (flow == PaymentFlow.Request) "Request created" else "Test payment successful", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(Modifier.height(8.dp))
                            Text("Reference: IGN-" + amount.ifBlank { "000" } + "-TEST", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f))
                        }
                    }
                    Button(onClick = onClose, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(18.dp)) {
                        Text("Back to Ignite Pay", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentSummaryRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f))
        Text(value, fontWeight = FontWeight.Bold)
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
