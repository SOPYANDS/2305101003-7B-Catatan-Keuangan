package com.example.catatankeuanganharianberbasisandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.catatankeuanganharianberbasisandroid.data.FinanceDatabase
import com.example.catatankeuanganharianberbasisandroid.data.TransactionEntity
import com.example.catatankeuanganharianberbasisandroid.data.TransactionRepository
import com.example.catatankeuanganharianberbasisandroid.ui.theme.CatatanKeuanganHarianBerbasisAndroidTheme
import kotlinx.coroutines.launch

// TEMA WARNA MODERN

private val Blue = Color(0xFF2563EB)
private val DarkBlue = Color(0xFF173B73)
private val LightBlue = Color(0xFFEAF2FF)
private val Background = Color(0xFFF5F7FB)
private val TextDark = Color(0xFF1E293B)
private val TextGray = Color(0xFF7B8798)
private val IncomeColor = Color(0xFF159B75)
private val ExpenseColor = Color(0xFFE05252)
private val White = Color.White

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            CatatanKeuanganHarianBerbasisAndroidTheme {
                val database = remember {
                    FinanceDatabase.getDatabase(applicationContext)
                }

                val repository = remember {
                    TransactionRepository(database.transactionDao())
                }

                FinanceApp(repository)
            }
        }
    }
}

// NAVIGASI UTAMA

@Composable
fun FinanceApp(repository: TransactionRepository) {
    LaunchedEffect(Unit) {
        repository.deleteTukuPurel()
    }

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    val menus = listOf(
        "Beranda",
        "Transaksi",
        "Riwayat",
        "Laporan"
    )

    val icons = listOf("⌂", "＋", "▤", "▥")

    Scaffold(
        containerColor = Background,
        bottomBar = {
            NavigationBar(
                containerColor = White,
                tonalElevation = 8.dp
            ) {
                menus.forEachIndexed { index, menu ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                        },
                        icon = {
                            Text(
                                text = icons[index],
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        label = {
                            Text(
                                text = menu,
                                fontSize = 11.sp,
                                fontWeight = if (selectedTab == index)
                                    FontWeight.Bold
                                else
                                    FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Blue,
                            selectedTextColor = Blue,
                            unselectedIconColor = TextGray,
                            unselectedTextColor = TextGray,
                            indicatorColor = LightBlue
                        )
                    )
                }
            }
        }
    ) { padding ->

        when (selectedTab) {
            0 -> DashboardScreen(
                repository,
                Modifier.padding(padding)
            )

            1 -> AddTransactionScreen(
                repository,
                Modifier.padding(padding)
            )

            2 -> TransactionHistoryScreen(
                repository,
                Modifier.padding(padding)
            )

            3 -> MonthlyReportScreen(
                repository,
                Modifier.padding(padding)
            )
        }
    }
}

// FORMAT RUPIAH

fun formatRupiah(amount: Long): String {
    return "Rp " + String.format(
        java.util.Locale("id", "ID"),
        "%,d",
        amount
    ).replace(',', '.')
}

// DASHBOARD MODERN

@Composable
fun DashboardScreen(
    repository: TransactionRepository,
    modifier: Modifier = Modifier
) {
    val totalIncome by repository.getTotalIncome()
        .collectAsState(initial = 0L)

    val totalExpense by repository.getTotalExpense()
        .collectAsState(initial = 0L)

    val transactions by repository.getAllTransactions()
        .collectAsState(initial = emptyList())

    val totalBalance = totalIncome - totalExpense

    val latestTransactions = transactions
        .sortedByDescending { it.id }
        .take(5)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Selamat Datang 👋",
            fontSize = 14.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Catatan Keuangan",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Text(
            text = "Kelola keuanganmu dengan lebih mudah.",
            fontSize = 13.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // KARTU SALDO GRADASI

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(
                containerColor = DarkBlue
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF2563EB),
                                Color(0xFF173B73)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "TOTAL SALDO",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Text(
                            "💳",
                            fontSize = 27.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        formatRupiah(totalBalance),
                        color = White,
                        fontSize = 29.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    HorizontalDivider(
                        color = White.copy(alpha = 0.25f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        "Saldo keuangan saat ini",
                        color = White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        SectionTitle(
            "Ringkasan Keuangan",
            "Pemasukan dan pengeluaranmu"
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FinanceSummaryCard(
                title = "Pemasukan",
                amount = formatRupiah(totalIncome),
                symbol = "↓",
                color = IncomeColor,
                modifier = Modifier.weight(1f)
            )

            FinanceSummaryCard(
                title = "Pengeluaran",
                amount = formatRupiah(totalExpense),
                symbol = "↑",
                color = ExpenseColor,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        SectionTitle(
            "Transaksi Terbaru",
            "Lima transaksi terakhir"
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (latestTransactions.isEmpty()) {
            EmptyState(
                title = "Belum ada transaksi",
                description = "Transaksi yang kamu tambahkan akan muncul di sini."
            )
        } else {
            latestTransactions.forEach { transaction ->
                TransactionItem(transaction)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
// JUDUL BAGIAN

@Composable
fun SectionTitle(
    title: String,
    subtitle: String? = null
) {
    Column {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        if (subtitle != null) {
            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextGray
            )
        }
    }
}

// KARTU RINGKASAN KEUANGAN

@Composable
fun FinanceSummaryCard(
    title: String,
    amount: String,
    symbol: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color.copy(alpha = 0.12f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = symbol,
                    fontSize = 24.sp,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = amount,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

// ITEM TRANSAKSI

@Composable
fun TransactionItem(
    transaction: TransactionEntity
) {
    val isIncome = transaction.type == "Pemasukan"

    val transactionName = if (transaction.note.isNotBlank()) {
        transaction.note
    } else {
        transaction.category
    }

    val amountText = if (isIncome) {
        "+ ${formatRupiah(transaction.amount)}"
    } else {
        "- ${formatRupiah(transaction.amount)}"
    }

    val amountColor = if (isIncome) {
        IncomeColor
    } else {
        ExpenseColor
    }

    val symbol = if (isIncome) "↓" else "↑"

    val iconBackground = if (isIncome) {
        Color(0xFFE3F7EF)
    } else {
        Color(0xFFFFE9E9)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        iconBackground,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = symbol,
                    color = amountColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transactionName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${transaction.category} • ${transaction.date}",
                    color = TextGray,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = amountText,
                color = amountColor,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

// TAMPILAN KETIKA DATA KOSONG

@Composable
fun EmptyState(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        LightBlue,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "≡",
                    fontSize = 30.sp,
                    color = Blue
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = description,
                color = TextGray,
                fontSize = 12.sp
            )
        }
    }
}

// HALAMAN RIWAYAT TRANSAKSI

@Composable
fun TransactionHistoryScreen(
    repository: TransactionRepository,
    modifier: Modifier = Modifier
) {
    val transactions by repository.getAllTransactions()
        .collectAsState(initial = emptyList())

    val sortedTransactions = transactions
        .sortedByDescending { it.id }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Aktivitas Keuangan",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Semua catatan transaksi kamu",
            fontSize = 13.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(22.dp))

        // KARTU TOTAL TRANSAKSI

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = DarkBlue
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF2563EB),
                                Color(0xFF173B73)
                            )
                        )
                    )
                    .padding(22.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total Transaksi",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    Text(
                        text = "${transactions.size} transaksi",
                        color = White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            Color.White.copy(alpha = 0.15f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "≡",
                        fontSize = 28.sp,
                        color = White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle(
            title = "Daftar Transaksi",
            subtitle = "Diurutkan dari transaksi terbaru"
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (sortedTransactions.isEmpty()) {
            EmptyState(
                title = "Belum Ada Transaksi",
                description = "Tambahkan transaksi untuk melihat riwayat."
            )
        } else {
            sortedTransactions.forEach { transaction ->
                TransactionItem(
                    transaction = transaction
                )
            }
        }
    }
}
// ==========================================
// HALAMAN TAMBAH TRANSAKSI
// ==========================================

@Composable
fun AddTransactionScreen(
    repository: TransactionRepository,
    modifier: Modifier = Modifier
) {

    var transactionType by remember {
        mutableStateOf("Pengeluaran")
    }

    var amount by remember {
        mutableStateOf("")
    }

    var category by remember {
        mutableStateOf("")
    }

    var date by remember {
        mutableStateOf(
            java.text.SimpleDateFormat(
                "dd/MM/yyyy",
                java.util.Locale.getDefault()
            ).format(java.util.Date())
        )
    }

    var note by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    var isSaving by remember {
        mutableStateOf(false)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    val categories = if (transactionType == "Pemasukan") {
        listOf(
            "Uang Saku",
            "Gaji",
            "Bonus",
            "Hadiah",
            "Lainnya"
        )
    } else {
        listOf(
            "Makanan",
            "Transportasi",
            "Pendidikan",
            "Internet",
            "Belanja",
            "Lainnya"
        )
    }

    val isSuccess = message == "Transaksi berhasil disimpan!"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Tambah Transaksi",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Catat setiap pemasukan dan pengeluaranmu.",
            fontSize = 13.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // PILIH JENIS TRANSAKSI

        SectionTitle("Jenis Transaksi")

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFE8EDF5),
                    RoundedCornerShape(15.dp)
                )
                .padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            listOf("Pemasukan", "Pengeluaran").forEach { type ->

                val selected = transactionType == type

                Surface(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        transactionType = type
                        category = ""
                        message = ""
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = if (selected) White else Color.Transparent,
                    shadowElevation = if (selected) 2.dp else 0.dp
                ) {

                    Text(
                        text = type,
                        modifier = Modifier.padding(
                            horizontal = 5.dp,
                            vertical = 14.dp
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = if (selected) Blue else TextGray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // NOMINAL

        FormLabel("Nominal")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    amount = it
                    message = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Contoh: 50000")
            },
            leadingIcon = {
                Text(
                    text = "Rp",
                    color = Blue,
                    fontWeight = FontWeight.Bold
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue,
                unfocusedBorderColor = Color(0xFFDCE3ED),
                focusedContainerColor = White,
                unfocusedContainerColor = White
            )
        )

        Spacer(modifier = Modifier.height(17.dp))

        // KATEGORI

        FormLabel("Kategori")

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Pilih kategori")
                },
                trailingIcon = {
                    TextButton(
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Text(
                            text = "▼",
                            color = Blue
                        )
                    }
                },
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,
                    unfocusedBorderColor = Color(0xFFDCE3ED),
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                categories.forEach { item ->

                    DropdownMenuItem(
                        text = {
                            Text(item)
                        },
                        onClick = {
                            category = item
                            expanded = false
                            message = ""
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(17.dp))

        // TANGGAL

        FormLabel("Tanggal")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = date,
            onValueChange = {
                date = it
                message = ""
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("DD/MM/YYYY")
            },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue,
                unfocusedBorderColor = Color(0xFFDCE3ED),
                focusedContainerColor = White,
                unfocusedContainerColor = White
            )
        )

        Spacer(modifier = Modifier.height(17.dp))

        // KETERANGAN

        FormLabel("Keterangan")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = note,
            onValueChange = {
                note = it
                message = ""
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Masukkan keterangan transaksi")
            },
            minLines = 3,
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue,
                unfocusedBorderColor = Color(0xFFDCE3ED),
                focusedContainerColor = White,
                unfocusedContainerColor = White
            )
        )

        Spacer(modifier = Modifier.height(25.dp))

        // TOMBOL SIMPAN

        Button(
            onClick = {

                val nominal = amount.toLongOrNull()

                when {

                    nominal == null || nominal <= 0L -> {
                        message = "Masukkan nominal yang valid."
                    }

                    category.isBlank() -> {
                        message = "Silakan pilih kategori."
                    }

                    date.isBlank() -> {
                        message = "Tanggal harus diisi."
                    }

                    isSaving -> {
                        // Sedang menyimpan.
                    }

                    else -> {

                        coroutineScope.launch {

                            isSaving = true
                            message = ""

                            try {

                                val transaction = TransactionEntity(
                                    type = transactionType,
                                    amount = nominal,
                                    category = category,
                                    date = date,
                                    note = note
                                )

                                repository.insert(transaction)

                                message = "Transaksi berhasil disimpan!"

                                amount = ""
                                category = ""
                                note = ""

                            } catch (e: Exception) {

                                message =
                                    "Gagal menyimpan transaksi: ${e.message}"

                            } finally {

                                isSaving = false
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            enabled = !isSaving,
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue,
                disabledContainerColor = Color.Gray
            ),
            shape = RoundedCornerShape(15.dp)
        ) {

            Text(
                text = if (isSaving) {
                    "Menyimpan..."
                } else {
                    "Simpan Transaksi"
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (message.isNotEmpty()) {

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSuccess) {
                        Color(0xFFE7F7F0)
                    } else {
                        Color(0xFFFFEEEE)
                    }
                ),
                shape = RoundedCornerShape(12.dp)
            ) {

                Text(
                    text = message,
                    modifier = Modifier.padding(14.dp),
                    color = if (isSuccess) {
                        IncomeColor
                    } else {
                        ExpenseColor
                    },
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// LABEL FORM

@Composable
fun FormLabel(text: String) {

    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextDark
    )
}

// ==========================================
// HALAMAN LAPORAN BULANAN
// ==========================================

@Composable
fun MonthlyReportScreen(
    repository: TransactionRepository,
    modifier: Modifier = Modifier
) {

    val transactions by repository.getAllTransactions()
        .collectAsState(initial = emptyList())

    val currentYear = java.util.Calendar.getInstance()
        .get(java.util.Calendar.YEAR)

    val currentMonth = java.util.Calendar.getInstance()
        .get(java.util.Calendar.MONTH)

    var selectedMonth by remember {
        mutableIntStateOf(currentMonth)
    }

    var selectedYear by remember {
        mutableIntStateOf(currentYear)
    }

    var monthExpanded by remember {
        mutableStateOf(false)
    }

    var yearExpanded by remember {
        mutableStateOf(false)
    }

    val monthNames = listOf(
        "Januari",
        "Februari",
        "Maret",
        "April",
        "Mei",
        "Juni",
        "Juli",
        "Agustus",
        "September",
        "Oktober",
        "November",
        "Desember"
    )

    val availableYears = (
            (currentYear - 5..currentYear + 1).toList() +
                    transactions.mapNotNull { transaction ->
                        transaction.date.split("/")
                            .takeIf { it.size == 3 }
                            ?.get(2)
                            ?.toIntOrNull()
                    }
            ).distinct().sortedDescending()

    val monthlyTransactions = transactions.filter { transaction ->

        val dateParts = transaction.date.split("/")

        if (dateParts.size == 3) {

            val month = dateParts[1].toIntOrNull()
            val year = dateParts[2].toIntOrNull()

            month == selectedMonth + 1 &&
                    year == selectedYear

        } else {
            false
        }

    }.sortedByDescending { it.id }

    val totalIncome = monthlyTransactions
        .filter { it.type == "Pemasukan" }
        .sumOf { it.amount }

    val totalExpense = monthlyTransactions
        .filter { it.type == "Pengeluaran" }
        .sumOf { it.amount }

    val totalBalance = totalIncome - totalExpense

    val expensePercentage = if (totalIncome > 0L) {
        (totalExpense.toDouble() / totalIncome.toDouble() * 100)
            .toInt()
    } else {
        0
    }

    val expenseProgress = if (totalIncome > 0L) {
        (totalExpense.toDouble() / totalIncome.toDouble())
            .toFloat()
            .coerceIn(0f, 1f)
    } else {
        0f
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Laporan Keuangan",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Pantau kondisi keuangan bulananmu.",
            fontSize = 13.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Periode Laporan")

        Spacer(modifier = Modifier.height(12.dp))

        // PILIH BULAN DAN TAHUN

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Box(
                modifier = Modifier.weight(1f)
            ) {

                OutlinedButton(
                    onClick = {
                        monthExpanded = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Blue
                    )
                ) {
                    Text(monthNames[selectedMonth])
                }

                DropdownMenu(
                    expanded = monthExpanded,
                    onDismissRequest = {
                        monthExpanded = false
                    }
                ) {

                    monthNames.forEachIndexed { index, month ->

                        DropdownMenuItem(
                            text = {
                                Text(month)
                            },
                            onClick = {
                                selectedMonth = index
                                monthExpanded = false
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {

                OutlinedButton(
                    onClick = {
                        yearExpanded = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Blue
                    )
                ) {
                    Text(selectedYear.toString())
                }

                DropdownMenu(
                    expanded = yearExpanded,
                    onDismissRequest = {
                        yearExpanded = false
                    }
                ) {

                    availableYears.forEach { year ->

                        DropdownMenuItem(
                            text = {
                                Text(year.toString())
                            },
                            onClick = {
                                selectedYear = year
                                yearExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // KARTU SALDO BULANAN

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(23.dp),
            colors = CardDefaults.cardColors(
                containerColor = DarkBlue
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF2563EB),
                                Color(0xFF173B73)
                            )
                        )
                    )
                    .padding(23.dp)
            ) {

                Text(
                    text = "SALDO BULAN INI",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = formatRupiah(totalBalance),
                    color = White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${monthNames[selectedMonth]} $selectedYear",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle(
            title = "Ringkasan Keuangan",
            subtitle = "Periode ${monthNames[selectedMonth]} $selectedYear"
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            FinanceSummaryCard(
                title = "Pemasukan",
                amount = formatRupiah(totalIncome),
                symbol = "↓",
                color = IncomeColor,
                modifier = Modifier.weight(1f)
            )

            FinanceSummaryCard(
                title = "Pengeluaran",
                amount = formatRupiah(totalExpense),
                symbol = "↑",
                color = ExpenseColor,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle(
            title = "Perbandingan Keuangan",
            subtitle = "Pemasukan dan pengeluaran bulan terpilih"
        )

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "Pemasukan",
                    fontSize = 12.sp,
                    color = TextGray
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = formatRupiah(totalIncome),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = IncomeColor
                )

                Spacer(modifier = Modifier.height(9.dp))

                LinearProgressIndicator(
                    progress = {
                        if (totalIncome > 0L) 1f else 0f
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(9.dp),
                    color = IncomeColor,
                    trackColor = Color(0xFFEDF1F6)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Pengeluaran",
                    fontSize = 12.sp,
                    color = TextGray
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = formatRupiah(totalExpense),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = ExpenseColor
                )

                Spacer(modifier = Modifier.height(9.dp))

                LinearProgressIndicator(
                    progress = {
                        expenseProgress
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(9.dp),
                    color = ExpenseColor,
                    trackColor = Color(0xFFEDF1F6)
                )

                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(
                    color = Color(0xFFEDF1F6)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Pengeluaran dibanding pemasukan",
                    fontSize = 12.sp,
                    color = TextGray
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "$expensePercentage%",
                    fontSize = 22.sp,
                    color = Blue,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle(
            title = "Transaksi Bulan Ini",
            subtitle = "${monthlyTransactions.size} transaksi tercatat"
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (monthlyTransactions.isEmpty()) {

            EmptyState(
                title = "Belum Ada Transaksi",
                description = "Tidak ada transaksi pada ${monthNames[selectedMonth]} $selectedYear."
            )

        } else {

            monthlyTransactions.forEach { transaction ->

                TransactionItem(
                    transaction = transaction
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}