import java.util.Scanner;

/*File ini sebagai program utama*/

public class MainProgram{
  public static void main(String[] args){
    int row = 0, col = 0, sumber;
    int i, j;
    double multiplier = 1;
    String solutiontype;
    MainSystem system = new MainSystem();
    TextReader baca = new TextReader();
    TextWriter tulis = new TextWriter();
    MainCalc calculation = new MainCalc();
    Matrix M = new Matrix(); //Sebagai matrix awal
    Matrix N = new Matrix(); //Matrix untuk dimanipulasi
    Scanner in = new Scanner(System.in);
    system.welcome(); //Menampilkan interface awal
    int pilihan = system.menu(); //Menyimpan pilihan SPL/Interpolasi/Keluar
    //--------------------------------------------------------------------------------------------------
    if(pilihan == 1){ //Jika pilihan menu ke-1 (SPL), jalanin augmented
      int metode = system.choice();
      sumber = system.source();
      //--------------------------------------------------------------------------------------------------
      if(sumber == 1){ //jika sumber dari keyboard
        System.out.print("Masukkan jumlah baris (termasuk baris b): ");
        row = in.nextInt();
        System.out.print("Masukkan jumlah kolom (termasuk kolom b): ");
        col = in.nextInt();
        M.isimatrix(row, col); //Mengisi matrix dari keyboard
        System.out.println();
        System.out.println("Matrix yang dimasukkan: ");
        M.tulismatrix(row, col); //Menulis matrix yang terbentuk ke layar
      //--------------------------------------------------------------------------------------------------
      } else{ //jika sumber dari file
        row = baca.getbaris("augmented.txt"); //Membaca banyak baris matrix dari file
        col = baca.getkolom("augmented.txt"); //Membaca banyak kolom matrix dari file
        M = baca.readFile("augmented.txt"); //Mengisi matrix M dengan matriks yang ada di file
        System.out.println();
        System.out.println("Isi dari Matrix: ");
        M.tulismatrix(row, col); //Menulis matrix yang terbentuk ke layar
      }
      //--------------------------------------------------------------------------------------------------
      if(metode == 1){ //Jika metode ke-1, jalanin gauss
        //gauss
        N = calculation.gauss(M, row, col); //Menyimpan matrix eselon hasil Gauss elimination
        System.out.println();
        System.out.println("Hasil matriks eselon:");
        N.tulismatrix(row, col); //Menulis hasil matrix eselon ke layar
      }
      //--------------------------------------------------------------------------------------------------
      else{ //Jika metode ke-2, jalanin gauss-jordan
        //gauss-jordan
        N = calculation.gaussjordan(M, row, col); //Menyimpan matrix eselon tereduksi hasil Gauss-Jordan elimination
        System.out.println();
        System.out.println("Hasil matriks eselon tereduksi:");
        N.tulismatrix(row, col); //Menulis hasil matrix eselon ke layar
      }
      //--------------------------------------------------------------------------------------------------
      tulis.writematrix(N, row, col, "augmented.txt");
      solutiontype = calculation.checksolution(N, row, col); //Menyimpan tipe solusi (inconsistent, unique, infinite)
      System.out.println();
      System.out.print("Matriks memiliki tipe solusi: ");
      System.out.println(solutiontype); //Menulis tipe solusi ke layar
      System.out.println();
      //--------------------------------------------------------------------------------------------------
      if(solutiontype == "inconsistent"){ //Jika tipe solusi inconsistent, maka tidak ada solusi dari SPL
        System.out.println("Matriks tidak memiliki solusi!");
        System.exit(0);
      }
      //--------------------------------------------------------------------------------------------------
      else if(solutiontype == "unique"){ //Jika tipe solusi unique, maka ada 1 solusi dari SPL
        double[] hasil = new double[col];
        String[] dummy = new String[1];
        dummy[0] = "unique";
        hasil = calculation.satusolusi(N, row, col); //Menyimpan hasil dari persamaan
        tulis.writefile(hasil, dummy, "augmented.txt", row, col-1); //Menuliskan hasil persamaan ke layar sekaligus menulis ke file
        System.exit(0);
      }
      //--------------------------------------------------------------------------------------------------
      else{ //Jika tipe solusi infinite, maka ada infinite solusi dari SPL
        //banyaksolusi
        String[] hasil = new String[col];
        double[] dummy = new double[0];
        hasil = calculation.banyaksolusi(N, row, col); //Menyimpan hasil dari persamaan
        tulis.writefile(dummy, hasil, "augmented.txt", row, col-1); //Menuliskan hasil persamaan ke layar sekaligus menulis ke file
      }
      //--------------------------------------------------------------------------------------------------
    } else if(pilihan == 2){ //Jika pilihan menu ke-2 (Interpolasi), jalanin interpolasi
      sumber = system.source();
      if(sumber == 1){ //jika sumber dari keyboard
        do{
          System.out.print("Masukkan jumlah pasangan point (>0): ");
          row = in.nextInt();
        } while(row < 1);
        M.isimatrix(row, 2); //Mengisi matrix dengan jumlah pasangan point
        System.out.println();
        System.out.println("Matrix yang dimasukkan: ");
        M.tulismatrix(row, 2); //Menulis isi matrix ke layar
        System.out.println();
      //--------------------------------------------------------------------------------------------------
      } else{ //jika sumber dari file
        row = baca.getbaris("interpolasi.txt"); //Membaca banyak baris matrix dari file
        M = baca.readFile("interpolasi.txt"); //Mengisi matrix M dengan matriks yang ada di file
        System.out.println();
        System.out.println("Isi dari Matrix: ");
        M.tulismatrix(row, 2); //Menulis isi matrix ke layar
        System.out.println();
      }
      //--------------------------------------------------------------------------------------------------
      if(row == 1){ //Jika hanya ada satu pasangan point, maka y akan tetap sama untuk semua x
        System.out.println("Baris harus lebih dari 1");
        System.exit(0);
      }
      //--------------------------------------------------------------------------------------------------
      else{
          col = row + 1; //jumlah kolom = jumlah baris + 1 karena akan terbentuk matriks augmented polinom
          for(i = 0; i < row; i++){ //Looping untuk membentuk matrix N sebagai sistem persamaan lanjar dari matriks M
            multiplier = 1;
            for(j = 0; j < col; j++){
              if(j > 0 && j < col - 1){
                multiplier *= M.content(i,0); //Nilai komponen point x dari setiap baris akan dikalikan berdasarkan pasangan point
              }
              if(j == 0){
                N.addel(i, j, 1);
              } else if(j == 1){
                N.addel(i, j, M.content(i, 0));
              } else if(j < col - 1){
                N.addel(i, j, multiplier);
              } else{
                N.addel(i, j, M.content(i, 1));
              }
            }
          }

        System.out.println("Sistem persamaan lanjar:");
        N.tulismatrix(row, col); //Menuliskan matrix sistem persamaan lanjar ke layar
        //interpolasi
        N = calculation.gauss(N, row, col); //Menyimpan matrix hasil kalkulasi menggunakan Gauss Elimination
        System.out.println();
        System.out.println("Hasil matriks eselon:");
        N.tulismatrix(row, col); //Menuliskan hasil matrix eselon ke layar
        tulis.writematrix(N, row, col, "interpolasi.txt");
        solutiontype = calculation.checksolution(N, row, col); //Menyimpan tipe solusi (inconsistent, unique, infinite)
        System.out.println();
        System.out.print("Matriks memiliki tipe solusi: ");
        System.out.println(solutiontype); //Menuliskan tipe solusi dari matrix ke layar (inconsistent, unique, infinite)
        System.out.println();
        //--------------------------------------------------------------------------------------------------
        if(solutiontype == "inconsistent"){ //Jika tipe solusi inconsistent, matrix tidak memiliki solusi
          System.out.println("Matriks tidak memiliki solusi!");
          System.exit(0);
        }
        //--------------------------------------------------------------------------------------------------
        else if(solutiontype == "unique"){ //Jika tipe solusi unique, matrix memiliki 1 solusi
          double[] hasil = new double[col];
          String[] dummy = new String[1];
          dummy[0] = "unique";
          hasil = calculation.satusolusi(N, row, col); //Menyimpan hasil dari persamaan
          tulis.writefile(hasil, dummy, "interpolasi.txt", row, col-1);
          System.exit(0);
        }
        //--------------------------------------------------------------------------------------------------
        else{ //Jika tipe solusi infinite, interpolasi tidak bisa dilakukan
          System.out.println("Hasil persamaan adalah parametrik");
          System.out.println("Tidak dapat dilakukan perkiraan nilai");
          System.exit(0);
        }
      }
    } else{ //Jika pilihan menu ke-3, maka program berhenti
      System.exit(0);
    }
  }
}
