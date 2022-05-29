public class Image {
    String codiPPM;
    String[][] pixels;
    int altura;
    int ancho;

    Image(String codiPPM){
        this.codiPPM = codiPPM;
        this.pixels = pixelsMaker(codiPPM);
        String [] tamaños = setTamaño(codiPPM);
        this.altura = Integer.parseInt(tamaños[1]);
        this.ancho = Integer.parseInt(tamaños[0]);
    }

    private String[] setTamaño(String codiPPM) {
        String[] codiSep = codiPPM.split("\n");
        if (codiSep[1].contains("#")){
            return codiSep[2].split(" ");

        } else{
            return codiSep[1].split(" ");

        }
    }

    private String[][] pixelsMaker(String codiPPM) {
        String[] codiSep = codiPPM.split("\n");
        int ancho = 0;
        int alto = 0;
        if (codiSep[1].contains("#")){
            String[] sizes = codiSep[2].split(" ");
            ancho = Integer.parseInt(sizes[0]);
            alto = Integer.parseInt(sizes[1]);
        } else{
            String[] sizes = codiSep[1].split(" ");
            ancho = Integer.parseInt(sizes[0]);
            alto = Integer.parseInt(sizes[1]);
        }
        int nPixels = alto*ancho;
        String[][] pixels =new String[nPixels][3];
        int index= 0;
        if (codiSep[1].contains("#")){
            index = 4;
        } else{
           index = 3;
        }
        for (int i = 0; i < nPixels; i++) {
            for (int j = 0; j < 3; j++) {
                pixels[i][j] = codiSep[index];
                index++;
            }
        }
        return pixels;
    }
}
