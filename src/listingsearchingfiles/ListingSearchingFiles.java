/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listingsearchingfiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Joco
 */
public class ListingSearchingFiles {

    public static void main(String[] args) {
        String rootFolder = "E:\\DirTest";

        System.out.print("Breadth first: \n"
                + "A szint azt jelzi milyen mélyen vagyunk az almappákban a gyokér könyvtárhoz képest:\n"
                + "minden szinten felsorolja az osszes elemet vesszővel elválasztva\n\n");
        System.out.println(listAllDirsBFS(new File(rootFolder)));

        System.out.print("Depth first: (itt most minden elem új sorban kezdődik)\n\n");
        System.out.println(listDirsDFS(new File(rootFolder)));

        System.out.print("Ez a verzió DFS elvén megy, csak másképpen vanak kiiratva a dolgok!\n"
                + "A szint azt jelzi milyen mélyen vagyunk az almappákban a gyokér könyvtárhoz képest:\n"
                + "Minden mappának amibe belép kiirja az elemeit egy sorban vesszovel elválasztva\n\n");
        System.out.println(listDirsDFS2(new File(rootFolder)));

        System.out.println("Keresés:");
        System.out.println(resultToString(searchDFS(new File(rootFolder), "sub2")));
//Fileba iratás:
//        printToFile("E:\\fajlok\\BFS.txt", listAllDirsBFS(new File(rootFolder)));
    }

    public static String listAllDirsBFS(File rootFile) { //Ezt a metódust kell meghívni, túl van töltve a paraméter ellenőrzése miatt
        if (rootFile.exists() && rootFile.isDirectory()) { //Itt ugye csak megnézzük, hogy létezik e a fájl, ha igen akkor mappa e ha ez mind stimmel mehet a listazas
            if (rootFile.listFiles().length > 0) {
                return listAllDirsBFS(1, rootFile.listFiles());
            } else {
                return "Nincs egy eleme sem a mappának\n";
            }
        } else {
            return "A fájl nem létezik vagy nem mappa\n";
        }
    }
    //Ez csak bemutatja hogy megy végig az algoritmus egy gráf elemein(itt konkretan ugye egy konyvtarszerkezeten)
    //nem jó megvalósitás mert nem tudnánk arra használni ebben a formában az algoritmust amire ténylegesen való 
    //de szerintem szemléltetésnek és kiindulásnak jó lehet

    public static String listAllDirsBFS(int level, File[] filesInDir) {
        StringBuilder output = new StringBuilder(); //StringBuildert itt jobb használni szerintem String helyett hogy miért? Azt tudni kéne de ha nem akkor ASK :P
        output.append("Szint ").append(level).append(": ");
        @SuppressWarnings("unchecked")
        List<File> subFolders = new ArrayList<>(); //létrehozunk egy uj listát, ebben tároljuk majd az összes elemet a következő szinten
        for (File file : filesInDir) { //végigmegyünk a mappa elemein
            output.append(file.getName()).append(", "); //itt ugye csak beleírjuk az elemeket az aktuális szinten
            if (file.isDirectory() && file.listFiles() != null) { //Ha az aktuális elem mappa és a listFiles metódus nem null-t ad vissza 
                subFolders.addAll(Arrays.asList(file.listFiles())); //beletesszük az összes elemét a listába amiben a kövi szint elemeit tároljuk
            }
        }
        level++; //Az aktuális szint kész mehet a kövi
        output.append("\n");
        if (subFolders.size() > 0) { //ha pakoltunk elemeket meghivjuk ugyan ezt a metodust
            filesInDir = subFolders.toArray(new File[subFolders.size()]); //mivel a paramétetr tömb és nem lista át kell alakítanunk, 
            output.append(listAllDirsBFS(level, filesInDir));//és bele is tesszük a paraméterként kapott, majd ezzel hivjuk meg ujra a metodust
        }
        return output.toString();
    }

    //Ez maga az algoritmus sima megvalositasa, csak bemutatja hogy mey vegig a mappákon, viszont az output az nem a legjobb, 
    //a lentebbi verzio ugyan ezen az elven van szebb outputtal
    public static String listDirsDFS(File rootFile) {//Ezt a metódust kell meghívni, túl van töltve egyrészt a paraméter ellenőrzése miatt
        if (rootFile.exists() && rootFile.isDirectory()) {//Itt ugye csak megnézzük, hogy létezik e a fájl, ha igen akkor mappa e
            if (rootFile.listFiles().length > 0) { //ha van benne valami meghivjuk a metodust, ha nincs kiirjuk
                return listDirsDFS(rootFile.listFiles());
            } else {
                return "Nincs egy eleme sem a mappának\n";
            }
        } else {
            return "A fájl nem létezik vagy nem mappa\n";
        }
    }

    private static String listDirsDFS(File[] filesInDir) {
        StringBuilder output = new StringBuilder();
        if (filesInDir.length > 0) {
            for (File file : filesInDir) {
                output.append(file.getName()).append('\n');
                if (file.isDirectory() && file.listFiles() != null) { // ha mappa és van is eleme meghivjuk ujra a metodust
                    output.append(listDirsDFS(file.listFiles()));//ugye itt mindig az első mappába megy bele majd azon belül is stb... output segít megérteni
                }
            }
        }
        return output.toString();
    }
//Itt másképpen iratjuk ki a mappákat de az elv ugyan az mint az előbbinél

    public static String listDirsDFS2(File rootFile) {//Ezt a metódust kell meghívni, túl van töltve egyrészt a paraméter ellenőrzése miatt
        if (rootFile.exists() && rootFile.isDirectory()) {
            if (rootFile.listFiles().length > 0) { //ha van benne valami meghivjuk a metodust, ha nincs kiirjuk
                return listDirsDFS2(rootFile.listFiles(), 0);
            } else {
                return "Nincs egy eleme sem a mappának\n";
            }
        } else {
            return "A fájl nem létezik vagy nem mappa\n";
        }
    }

    private static String listDirsDFS2(File[] filesInDir, int level) {
        StringBuilder output = new StringBuilder();
        level++;
        if (filesInDir.length > 0) {
            output.append("Szint ").append(level).append(": ");
            for (File file : filesInDir) {  //A fentihez képest az egyetlen különbség itt van, az aktuális mappa összes elemét beletesszük az outPutba
                output.append(file.getName()).append(',');
            }
            output.append("\n");
            for (File file : filesInDir) {
                if (file.isDirectory() && file.listFiles() != null) {
                    output.append(listDirsDFS2(file.listFiles(), level));
                }
            }
        }
        return output.toString();
    }

//Keresés a DFS algoritmussal - ugyan az az elv mint a listázás csak itt vizsgáljuk a fájlok nevét és az output nem egy stringbe lesz hanem egy fájl tipusu listában
//Ha a paraméterként megadott file nem helyes nullt ad vissza, ha nem talál elemet akkor a lista mérete nulla lessz     
    public static List<File> searchDFS(File rootFile, String search) {
        if (rootFile.isDirectory() && rootFile.exists()) {
            search = ".*" + search + ".*"; //ez itt egyelőre gagyi pattern de nem ez a lényeg most :D
            return searchDFS(rootFile.listFiles(), search);
        } else {
            return null;
        }
    }

    private static List<File> searchDFS(File[] rootDir, String pattern) {
        @SuppressWarnings("unchecked")
        List<File> result = new ArrayList();
        if (rootDir.length > 0) {
            for (File file : rootDir) {
                if (file.getName().matches(pattern)) {
                    result.add(file);
                }
                if (file.isDirectory() && file.listFiles() != null) {
                    result.addAll(searchDFS(file.listFiles(), pattern));
                }
            }
        }
        return result;
    }
//itt átalakitjuk a keresés eredményét Stringbe hogy valamit ki tudjunk irni

    public static String resultToString(List<File> result) {
        if (result != null) {
            if (result.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (File file : result) {
                    sb.append(file).append("\n");
                }
                return sb.toString();
            }
            return "Semmit sem találtam =(\n";
        }
        return "A fájl nem létezik vagy nem mappa\n";
    }

    //String kiiratása egy fájlba:
    public static void printToFile(String targetFilePath, String output) {
        try (FileWriter writer = new FileWriter(targetFilePath, true)) {
            writer.append(output);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
