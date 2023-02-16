import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {

        GameProgress player1 = new GameProgress(100, 10, 80, 250.4);
        GameProgress player2 = new GameProgress(100, 9, 79, 250.4);
        GameProgress player3 = new GameProgress(100, 6, 75, 250.4);



        String dir = "/Users/realwonder/Games/savegames/save.dat";
        String zip = "/Users/realwonder/Games/savegames/zip.zip";

        //Записываем объект в файл
        saveGame(dir, player2);
        //Архивируем файл
        zipFiles(zip, dir);

        //Удаляем не архивированный файл
        if(new File(dir).delete()){
            System.out.println("файл по адресу "+ dir + " удален!");
        }

        //читаем архив в файл
        openZip(zip, dir);

        //читаем байты в объект и выводим его
        System.out.println(openProgress(dir));


    }

    public static void saveGame(String file, GameProgress player) {
        try (FileOutputStream fos = new FileOutputStream(file, true);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(player);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String file) {

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static void zipFiles(String zip, String file) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zip));
             FileInputStream fis = new FileInputStream(file)) {
            ZipEntry entry = new ZipEntry("packed_save.dat");
            zout.putNextEntry(entry);

            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);

            zout.write(buffer);
            zout.closeEntry();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    public static void openZip(String zip, String file) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zip))) {
            while ((zin.getNextEntry()) != null) {
                FileOutputStream fout = new FileOutputStream(file);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}

