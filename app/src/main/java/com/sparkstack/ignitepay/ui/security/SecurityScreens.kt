package com.sparkstack.ignitepay.ui.security

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class SecurityDestination {
    Hub, PinSetup, PinConfirm, PinEntry, ChangePin, PinLocked, Biometrics, Devices, Activity, ForgotPin, SessionExpired
}

@Composable
fun SecurityScreen(
    destination: SecurityDestination,
    demoPin: String?,
    onNavigate: (SecurityDestination) -> Unit,
    onPinConfigured: (String) -> Unit,
    onSignOut: () -> Unit,
    onClose: () -> Unit
) {
    var pinDraft by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var pinEntry by remember { mutableStateOf("") }
    var attempts by remember { mutableIntStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }

    val title = when (destination) {
        SecurityDestination.Hub -> "Security"
        SecurityDestination.PinSetup -> "Create your PIN"
        SecurityDestination.PinConfirm -> "Confirm your PIN"
        SecurityDestination.PinEntry -> "Enter your PIN"
        SecurityDestination.ChangePin -> "Verify current PIN"
        SecurityDestination.PinLocked -> "Security lock"
        SecurityDestination.Biometrics -> "Biometric unlock"
        SecurityDestination.Devices -> "Trusted devices"
        SecurityDestination.Activity -> "Security activity"
        SecurityDestination.ForgotPin -> "Forgot PIN"
        SecurityDestination.SessionExpired -> "Session expired"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (destination == SecurityDestination.Hub) onClose()
                        else onNavigate(SecurityDestination.Hub)
                    }) {
                        Icon(Icons.Outlined.ArrowBack, "Back")
                    }
                },
                actions = {
                    Text("TEST", color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 16.dp))
                }
            )
        }
    ) { padding ->
        when (destination) {
            SecurityDestination.Hub -> SecurityHub(
                modifier = Modifier.padding(padding),
                pinConfigured = demoPinConfigured,
                onPin = { onNavigate(if (demoPin != null) SecurityDestination.PinEntry else SecurityDestination.PinSetup) },
                onBiometrics = { onNavigate(SecurityDestination.Biometrics) },
                onDevices = { onNavigate(SecurityDestination.Devices) },
                onActivity = { onNavigate(SecurityDestination.Activity) },
                onForgot = { onNavigate(SecurityDestination.ForgotPin) },
                onSessionExpired = { onNavigate(SecurityDestination.SessionExpired) }
            )
            SecurityDestination.PinSetup -> SecurityPinEntry(
                modifier = Modifier.padding(padding),
                title = "Create a 4–6 digit PIN",
                description = "This test PIN exists only in memory for this preview. It is not securely stored.",
                value = pinDraft,
                onValueChange = { pinDraft = it; error = null },
                buttonLabel = "Continue",
                error = error,
                onContinue = {
                    if (pinDraft.length in 4..6) {
                        confirmPin = ""
                        onNavigate(SecurityDestination.PinConfirm)
                    } else error = "Use 4 to 6 digits."
                }
            )
            SecurityDestination.PinConfirm -> SecurityPinEntry(
                modifier = Modifier.padding(padding),
                title = "Confirm your PIN",
                description = "Re-enter the same test PIN to complete the simulated setup.",
                value = confirmPin,
                onValueChange = { confirmPin = it; error = null },
                buttonLabel = "Save test PIN",
                error = error,
                onContinue = {
                    when {
                        confirmPin.length !in 4..6 -> error = "Use 4 to 6 digits."
                        confirmPin != pinDraft -> error = "PINs do not match."
                        else -> onPinConfigured(pinDraft)
                    }
                }
            )
            SecurityDestination.PinEntry -> SecurityPinEntry(
                modifier = Modifier.padding(padding),
                title = "Verify your PIN",
                description = "Enter the test PIN currently held in this app session.",
                value = pinEntry,
                onValueChange = { pinEntry = it; error = null },
                buttonLabel = "Verify",
                error = error,
                onContinue = {
                    if (pinEntry.length !in 4..6) {
                        error = "Enter 4 to 6 digits."
                    } else if (demoPin == null) {
                        error = "No test PIN has been configured."
                    } else if (pinEntry == demoPin) {
                        error = null
                        onNavigate(SecurityDestination.Hub)
                    } else {
                        error = "Incorrect test PIN."
                        attempts += 1
                        if (attempts >= 3) onNavigate(SecurityDestination.PinLocked)
                    }
                },
                secondaryLabel = "Forgot PIN?",
                onSecondary = { onNavigate(SecurityDestination.ForgotPin) }
            )
            SecurityDestination.ChangePin -> SecurityPinEntry(
                modifier = Modifier.padding(padding),
                title = "Verify current PIN",
                description = "Confirm the current test PIN before choosing a replacement.",
                value = pinEntry,
                onValueChange = { pinEntry = it; error = null },
                buttonLabel = "Continue",
                error = error,
                onContinue = {
                    if (demoPin == null) error = "No test PIN has been configured."
                    else if (pinEntry == demoPin) {
                        pinDraft = ""
                        confirmPin = ""
                        error = null
                        onNavigate(SecurityDestination.PinSetup)
                    } else {
                        error = "Incorrect test PIN."
                        attempts += 1
                        if (attempts >= 3) onNavigate(SecurityDestination.PinLocked)
                    }
                }
            )
            SecurityDestination.PinLocked -> SecurityMessageScreen(
                modifier = Modifier.padding(padding),
                icon = Icons.Outlined.Lock,
                heading = "PIN temporarily locked",
                body = "Three incorrect attempts were detected in this test session. A production implementation will enforce server/device security controls.",
                primaryLabel = "Back to security",
                onPrimary = { onNavigate(SecurityDestination.Hub) }
            )
            SecurityDestination.Biometrics -> SecurityBiometricScreen(
                modifier = Modifier.padding(padding),
                onBack = { onNavigate(SecurityDestination.Hub) }
            )
            SecurityDestination.Devices -> SecurityListScreen(
                modifier = Modifier.padding(padding),
                heading = "Trusted devices",
                description = "Production device trust will be managed by the backend. This preview shows the intended surface only.",
                icon = Icons.Outlined.Devices,
                rows = listOf(
                    "This Android device" to "Current test session · Active",
                    "No other trusted devices" to "Production device management coming later"
                ),
                onBack = { onNavigate(SecurityDestination.Hub) }
            )
            SecurityDestination.Activity -> SecurityListScreen(
                modifier = Modifier.padding(padding),
                heading = "Security activity",
                description = "A future audit trail will show authentication and device events.",
                icon = Icons.Outlined.Security,
                rows = listOf(
                    "Test session started" to "Today · Frontend preview",
                    "Security settings opened" to "Today · Frontend preview",
                    "No live security events" to "Test mode only"
                ),
                onBack = { onNavigate(SecurityDestination.Hub) }
            )
            SecurityDestination.ForgotPin -> SecurityMessageScreen(
                modifier = Modifier.padding(padding),
                icon = Icons.Outlined.HelpOutline,
                heading = "PIN recovery is not connected",
                body = "A production recovery flow will require verified identity and backend-controlled recovery. No credentials are reset by this preview.",
                primaryLabel = "Back to security",
                onPrimary = { onNavigate(SecurityDestination.Hub) }
            )
            SecurityDestination.SessionExpired -> SecurityMessageScreen(
                modifier = Modifier.padding(padding),
                icon = Icons.Outlined.Timer,
                heading = "Session expired",
                body = "This simulated state represents what the app should show when a production authentication session expires.",
                primaryLabel = "Return to security",
                onPrimary = { onNavigate(SecurityDestination.Hub) },
                secondaryLabel = "Sign out test session",
                onSecondary = onSignOut
            )
        }
    }
}

@Composable
private fun SecurityHub(
    modifier: Modifier,
    pinConfigured: Boolean,
    onPin: () -> Unit,
    onBiometrics: () -> Unit,
    onDevices: () -> Unit,
    onActivity: () -> Unit,
    onForgot: () -> Unit,
    onSessionExpired: () -> Unit
) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Spacer(Modifier.height(12.dp))
            Text("Protect your account", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text("Security controls are represented as frontend states in this test build. No PIN or biometric secret is stored securely yet.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f), lineHeight = 20.sp)
        }
        item {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.VerifiedUser, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp))
                    Column(Modifier.padding(start = 14.dp)) {
                        Text("TEST MODE SECURITY", fontWeight = FontWeight.ExtraBold)
                        Text("No live authentication or financial access is controlled by these screens.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f))
                    }
                }
            }
        }
        item { SecurityOption("PIN", if (pinConfigured) "Configured for this test session" else "Not configured", Icons.Outlined.Pin, onPin) }
        if (pinConfigured) item { SecurityOption("Change PIN", "Verify the current test PIN first", Icons.Outlined.Edit, { onNavigate(SecurityDestination.ChangePin) }) }
        item { SecurityOption("Biometric unlock", "Ready for a future device-backed implementation", Icons.Outlined.Fingerprint, onBiometrics) }
        item { SecurityOption("Trusted devices", "Review device trust state", Icons.Outlined.Devices, onDevices) }
        item { SecurityOption("Security activity", "Authentication and device events", Icons.Outlined.Security, onActivity) }
        item { SecurityOption("Forgot PIN", "Production recovery placeholder", Icons.Outlined.HelpOutline, onForgot) }
        item {
            OutlinedButton(onClick = onSessionExpired, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)) {
                Icon(Icons.Outlined.Timer, null)
                Spacer(Modifier.width(8.dp))
                Text("Simulate session expiry")
            }
        }
    }
}

@Composable
private fun SecurityOption(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth().height(70.dp), shape = RoundedCornerShape(16.dp), contentPadding = PaddingValues(horizontal = 16.dp)) {
        Icon(icon, null)
        Column(Modifier.weight(1f).padding(start = 12.dp), horizontalAlignment = Alignment.Start) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.56f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(Icons.Outlined.ChevronRight, null)
    }
}

@Composable
private fun SecurityPinEntry(
    modifier: Modifier,
    title: String,
    description: String,
    value: String,
    onValueChange: (String) -> Unit,
    buttonLabel: String,
    error: String?,
    onContinue: () -> Unit,
    secondaryLabel: String? = null,
    onSecondary: (() -> Unit)? = null
) {
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Spacer(Modifier.height(12.dp))
        Surface(Modifier.size(64.dp), shape = CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)) {
            Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.Pin, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp)) }
        }
        Text(title, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Text(description, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f), lineHeight = 20.sp)
        OutlinedTextField(
            value = value,
            onValueChange = { input -> if (input.all(Char::isDigit) && input.length <= 6) onValueChange(input) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("PIN") },
            leadingIcon = { Icon(Icons.Outlined.Lock, null) },
            supportingText = { Text(error ?: "4–6 digits · test mode only") },
            isError = error != null,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(Modifier.weight(1f))
        Button(onClick = onContinue, enabled = value.length in 4..6, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(18.dp)) {
            Text(buttonLabel, fontWeight = FontWeight.Bold)
        }
        if (secondaryLabel != null && onSecondary != null) {
            TextButton(onClick = onSecondary, modifier = Modifier.fillMaxWidth()) { Text(secondaryLabel) }
        }
    }
}

@Composable
private fun SecurityBiometricScreen(modifier: Modifier, onBack: () -> Unit) {
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(44.dp))
        Surface(Modifier.size(92.dp), shape = CircleShape, color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.14f)) {
            Box(contentAlignment = Alignment.Center) { Icon(Icons.Outlined.Fingerprint, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(52.dp)) }
        }
        Text("Biometric unlock", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Text("The production app can use Android's biometric APIs for device-backed authentication. This build only presents the intended flow.", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f), lineHeight = 20.sp)
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Ready for integration", fontWeight = FontWeight.Bold)
                Text("No biometric credential is read, stored, or bypassed by this test screen.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f))
            }
        }
        Spacer(Modifier.weight(1f))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(18.dp)) { Text("Back to security", fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun SecurityListScreen(
    modifier: Modifier,
    heading: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    rows: List<Pair<String, String>>,
    onBack: () -> Unit
) {
    LazyColumn(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(Modifier.size(48.dp), shape = CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)) {
                    Box(contentAlignment = Alignment.Center) { Icon(icon, null, tint = MaterialTheme.colorScheme.primary) }
                }
                Column(Modifier.padding(start = 14.dp)) {
                    Text(heading, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                    Text(description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f))
                }
            }
        }
        items(rows) { (title, subtitle) ->
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(title, fontWeight = FontWeight.SemiBold)
                    Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f))
                }
            }
        }
        item { OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)) { Text("Back to security") } }
    }
}

@Composable
private fun SecurityMessageScreen(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    heading: String,
    body: String,
    primaryLabel: String,
    onPrimary: () -> Unit,
    secondaryLabel: String? = null,
    onSecondary: (() -> Unit)? = null
) {
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(48.dp))
        Surface(Modifier.size(84.dp), shape = CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)) {
            Box(contentAlignment = Alignment.Center) { Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(42.dp)) }
        }
        Text(heading, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
        Text(body, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f), lineHeight = 20.sp)
        Spacer(Modifier.weight(1f))
        Button(onClick = onPrimary, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(18.dp)) { Text(primaryLabel, fontWeight = FontWeight.Bold) }
        if (secondaryLabel != null && onSecondary != null) TextButton(onClick = onSecondary, modifier = Modifier.fillMaxWidth()) { Text(secondaryLabel) }
    }
}
