# Pengumpulan UTS Pemrograman Mobile

## Identitas Mahasiswa

- Nama: Sofyan Dwi Saputro
- NIM: 2305101003
- Kelas: 7B
- Program Studi: Teknik Informatika
- Mata Kuliah: Pemrograman Bergerak
- Tahun Akademik: 2026/2027

## Deskripsi

Repository ini berisi source code, dokumentasi, dan hasil implementasi
aplikasi mobile untuk pengerjaan Ujian Tengah Semester (UTS)
mata kuliah Pemrograman Mobile.

## Deskripsi Pengerjaan UTS

Aplikasi ini dibuat untuk membantu pengguna mencatat pengeluaran dan pemasukan harian secara sederhana dan terstruktur. Fitur utama dari aplikasi ini adalah menambahkan transaksi, melihat riwayat catatan keuangan, serta menghitung saldo harian berdasarkan pemasukan dan pengeluaran. Aplikasi ini dirancang agar mudah digunakan dan cocok untuk kebutuhan pencatatan keuangan pribadi.

## Teknologi yang Digunakan

- Bahasa Pemrograman: Kotlin
- Framework: Android Native (Jetpack Compose / Android UI)
- Tools: Android Studio, Gradle, Git
- Database: Room Database (jika digunakan dalam proyek), atau data lokal berbasis SharedPreferences / SQLite

## Fitur yang Diimplementasikan

1. Menambah transaksi pemasukan dan pengeluaran
2. Menampilkan daftar riwayat keuangan harian
3. Menghitung saldo secara otomatis berdasarkan data transaksi
4. Menyimpan data secara lokal agar tetap tersedia saat aplikasi dibuka kembali

## Struktur Folder

- `app/`: Source code utama aplikasi Android.
- `app/src/main/java/`: Kode program aplikasi.
- `app/src/main/res/`: Resource aplikasi seperti layout, strings, dan ikon.
- `app/src/test/`: Unit test.
- `screenshots/`: Bukti visual hasil implementasi aplikasi.

## Persyaratan Menjalankan Aplikasi

- Android Studio
- JDK 17 atau versi yang sesuai
- Android SDK yang sudah terinstall
- Emulator atau perangkat Android fisik
- Gradle dan dependency project yang sudah terunduh otomatis saat build

## Cara Menjalankan Aplikasi

1. Buka project di Android Studio.
2. Pastikan semua dependency sudah terunduh melalui Gradle Sync.
3. Pilih emulator atau perangkat Android yang terhubung.
4. Tekan tombol Run atau jalankan perintah berikut:
   ```bash
   ./gradlew assembleDebug
   ```
5. Aplikasi akan terinstall dan dijalankan pada emulator/perangkat Android.

## Screenshot Hasil Implementasi

Cantumkan dokumentasi screenshot hasil implementasi aplikasi mobile.

## File APK

File APK dapat dibuat melalui build debug dari Android Studio atau Gradle. Jika diperlukan, file APK dapat diambil dari folder build keluaran setelah proses build selesai.
