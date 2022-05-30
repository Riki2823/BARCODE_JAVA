
// Consultar taula https://en.wikipedia.org/wiki/Barcode#Linear_barcodes
// Code11: https://en.wikipedia.org/wiki/Code_11

// Generadors de codis:
//     https://barcode.tec-it.com/en/Code11
//     https://www.free-barcode-generator.net/code-11/
//     https://products.aspose.app/barcode/generate


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Code11 {

    // Codifica un String amb Code11
    static String encode(String s) {
        char[] sChars = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            sChars[i] = s.charAt(i);
        }
        String on = "█";
        String off = " ";

        String barcode = setBarrCode(sChars);

        String result = "";
        String[] bits = barcode.split("");
        for (int i = 0; i < bits.length; i++) {
            if (bits[i].equals(off)) {
                result += off;
            } else if (bits[i].equals(on)) {
                result += on;
            } else if (bits[i].equals("-")) {
                result += off;
            }
        }
        return result;
    }

    /*Tenemos una fucion a la que pasamos un array con los caracteres de nuestro codigo inicial y en funcion de un caracter u otro vamos sumando
      Cadenas de simbolos a el string que devolvemos a la funcion encode*/
    private static String setBarrCode(char[] sChars) {
        String barcode = "";
        for (int i = 0; i < sChars.length; i++) {
            String aux = "";

            switch (sChars[i]) {
                case '*':
                    aux = "█ ██  █";
                    break;
                case '0':
                    aux = "█ █ ██";
                    break;
                case '1':
                    aux = "██ █ ██";
                    break;
                case '2':
                    aux = "█  █ ██";
                    break;
                case '3':
                    aux = "██  █ █";
                    break;
                case '4':
                    aux = "█ ██ ██";
                    break;
                case '5':
                    aux = "██ ██ █";
                    break;
                case '6':
                    aux = "█  ██ █";
                    break;
                case '7':
                    aux = "█ █  ██";
                    break;
                case '8':
                    aux = "██ █  █";
                    break;
                case '9':
                    aux = "██ █ █";
                    break;
                case '-':
                    aux = "█ ██ █";
                    break;
            }
            if (i == sChars.length - 1) {
                barcode += aux;
            } else {
                barcode += aux + "-";
            }
        }
        return barcode;
    }

    // Decodifica amb Code11
    static String decode(String s) {
        if (s.equals("")) {
            return null;
        }
        String initialCode = s.trim();

        String[] barcode = barCodeCode(initialCode);

        String barcodeS = String.join("9", barcode);

        int big = setMesures(barcodeS, 1);
        int little = setMesures(barcodeS, 2);

        int aux = 0;

        String result = setFinalCode(big, barcodeS, aux);
        if (result.equals("ERROR")) {
            return null;
        }


        if (result.charAt(0) != '*') {
            boolean state = true;
            while (state) {
                if (aux >= big) {
                    return null;
                }
                aux++;
                result = setFinalCode(big, barcodeS, aux);
                if (result.charAt(0) == '*') {
                    state = false;
                }
            }
        }

        return result;
    }

    /*Tenemos una funcion que lo que haces es traducir el string con caracteres de barras a un array de string en el que tenemos un 1 cuando encontramos
     * una barra, un 0 cuando encontramos un espacio y un 9 cuando encontramos un espacion que nos indica que hemos cambiado de caracter, por ejemplo como en el codigo *0* tenemos 3 caracteres en nuestro array
     * tendremos dos 9 que nos indican la separacion del * con el 0 y la separacion del 0 con el segundo * */
    private static String[] barCodeCode(String s) {
        /*
         * false = espai
         * true = barra
         * */
        boolean estate = true;
        int[] barcode = new int[s.length()];
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            char aux = s.charAt(i);
            if (estate) {
                if (aux == '█') {
                    barcode[i] = 1;
                } else {
                    if (count == 4) {
                        boolean state = true;
                        while (state) {
                            int coaux = i;
                            char aux2 = s.charAt(coaux);
                            if (aux2 == ' ') {
                                barcode[coaux] = 9;
                                coaux++;
                            } else {
                                barcode[coaux] = 1;
                                state = false;
                            }
                            i = coaux;

                        }
                        count = 0;
                    } else {
                        count++;
                        estate = false;
                        barcode[i] = 0;
                    }
                }
            }
            if (!estate) {
                if (aux == ' ') {
                    barcode[i] = 0;
                } else {
                    count++;
                    barcode[i] = 1;
                    estate = true;
                }
            }

        }

        return passToArray(barcode);
    }

    //Una funcion que nos permite determinar cual es el numero maximo de "pixeles" que tiene nuestro string al querer indicar parte gruesa
    private static int setMesures(String barcodeS, int type) {
        List<Integer> sizes = sizeColector(barcodeS);
        int big = 0;
        int small = 10;
        for (int i = 0; i < sizes.size(); i++) {
            if (sizes.get(i) >= big) {
                big = sizes.get(i);
            }
            if (sizes.get(i) <= small && sizes.get(i) != 0) {
                small = sizes.get(i);
            }
        }
        if (type == 1) {
            return big;
        } else if (type == 2) {
            return small;
        }

        return 0;
    }

    /*En esta funcion encontramos diversas funcionalidades pero la principal es la de pasar el codigo tipo string que nos mide pixel por pixel el string original a un codigo de 5 digitos que nos indica el grosor de un espacio
     * o parte dibujada en funcion de su valor y posicion de estos valores*/
    private static String setFinalCode(int big, String barcodeS, int aux) {
        String code = "";
        //Lo primero que hacemos es llamar a una funcion que nos pasa el string de codigo a una lista con todos los tamaños posibles de el string
        List<Integer> sizes = sizeColector(barcodeS);

        // Si alguno de los valores de la lista es 0 es necesario eliminarlo para evitar errores en un futuro
        sizes.removeAll(Collections.singleton(0));

        //Si una vez eliminados los 0s el tamaño de nuestra lista no se puede dividir de manera exacta entre 0 esto nos indica que se ha leido mal el codigo
        if (sizes.size() % 5 != 0) {
            return "ERROR";
        }

        //En este string vamos a guardar los codigos de cada caracter que queremos decodificar
        String[] codes = makeCodes(sizes, big, code, aux);

        StringBuilder strBuilded = new StringBuilder(barcodeS);
        if (!codes[0].equals("00110")) {
            StringBuilder strReveresed = strBuilded.reverse();
            barcodeS = strReveresed.toString();
            code = "";
            sizes = sizeColector(barcodeS);
            sizes.removeAll(Collections.singleton(0));
            codes = makeCodes(sizes, big, code, aux);
        }
        //Llamamos a esta funcion que nos consigue dar en funcion de los codigos que pasamos el string final con el codigo ya decodificado
        String finalCode = listCodes(codes);
        return finalCode;
    }

    private static String[] makeCodes(List<Integer> sizes, int big, String code, int aux) {
        String[] codes = new String[(sizes.size() / 5)];

        int index = 0;

        big = big - aux;

        for (int i = 0; i < sizes.size(); i++) {
            if (code.length() == 5) {
                codes[index] = code;
                code = "";
                index++;
            }
            if (sizes.get(i) >= big) {
                code += "1";
            } else {
                code += "0";
            }
        }
        codes[index] = code;
        return codes;
    }


    //La funcion que nos permite segun los codigos definidos en setFinalCode sacar el codigo ya decodificado
    private static String listCodes(String[] codes) {
        String finalCode = "";
        for (int i = 0; i < codes.length; i++) {
            String aux = "";
            switch (codes[i]) {
                case "00001":
                    aux = "0";
                    break;
                case "10001":
                    aux = "1";
                    break;
                case "01001":
                    aux = "2";
                    break;
                case "11000":
                    aux = "3";
                    break;
                case "00101":
                    aux = "4";
                    break;
                case "10100":
                    aux = "5";
                    break;
                case "01100":
                    aux = "6";
                    break;
                case "00011":
                    aux = "7";
                    break;
                case "10010":
                    aux = "8";
                    break;
                case "10000":
                    aux = "9";
                    break;
                case "00100":
                    aux = "-";
                    break;
                case "00110":
                    aux = "*";
                    break;
                default:
                    aux = "E";
            }
            finalCode += aux;
        }
        return finalCode;
    }

    //La funcion que nos permite generar una lista con todos los tamaños disponibles dentro dentro de nuestro codigo
    private static List<Integer> sizeColector(String barcodeS) {
        boolean status = true;
        List<Integer> sizes = new ArrayList<Integer>();
        int count = 0;
        for (int i = 0; i < barcodeS.length(); i++) {
            char aux = barcodeS.charAt(i);
            if (status) {
                if (aux == '1') {
                    count++;
                } else if (aux == '0') {
                    sizes.add(count);
                    count = 1;
                    status = false;
                } else if (aux == '9') {
                    sizes.add(count);
                    count = 0;
                }
            } else {
                if (aux == '0') {
                    count++;
                } else if (aux == '1') {
                    sizes.add(count);
                    count = 1;
                    status = true;
                }
            }
        }
        sizes.add(count);
        return sizes;
    }

    private static String[] passToArray(int[] barcode) {
        String string = "";
        for (int i = 0; i < barcode.length; i++) {
            string += Integer.toString(barcode[i]);
        }
        return string.split("9");
    }

    // Decodifica una imatge. La imatge ha d'estar en format "ppm"
    public static String decodeImage(String str) {
        String aux = "";
        str = str.replace("\r", "");
        Image image = new Image(str);
        boolean vertical = false;
        int[][] indvPixel = setIndividualPixel(image);
        String result = "";


        for (int i = 0; i < image.altura; i++) {
            for (int j = 0; j < image.ancho; j++) {
                if (indvPixel[i][j] >= 100) {
                    aux += " ";
                } else {
                    aux += "█";
                }
            }
            aux = aux.trim();
            if (aux.equals(" ")) {
                continue;
            }
            result = decode(aux);
            if (result == null) {
                aux = "";
            } else if (result.contains("E")) {
                aux = "";
            } else {
                return result;
            }
        }
        for (int i = 0; i < image.altura; i++) {
            if (indvPixel[i][image.ancho / 2] >= 100) {
                aux += " ";
            } else {
                aux += "█";
            }

        }
        aux = aux.trim();
        result = decode(aux);
        if (result == null) {
            aux = "";
        } else if (result.contains("E")) {
            aux = "";
        } else {
            return result;
        }
        return null;
    }

    private static int[][] setIndividualPixel(Image pixels) {
        int[][] indvPixel = new int[pixels.altura][pixels.ancho];
        int index = 0;
        for (int i = 0; i < pixels.altura; i++) {
            for (int j = 0; j < pixels.ancho; j++) {
                int red = Integer.parseInt(pixels.pixels[index][0]);
                int green = Integer.parseInt(pixels.pixels[index][1]);
                int blue = Integer.parseInt(pixels.pixels[index][2]);
                int RGB = (red + green + blue) / 3;
                indvPixel[i][j] = RGB;
                index++;
            }
        }
        return indvPixel;
    }

    // Genera imatge a partir de codi de barres
    // Alçada: 100px
    // Marges: vertical 4px, horizontal 8px
    public static String generateImage(String s) {
        return "";
    }
}
