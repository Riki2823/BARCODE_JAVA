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
        String[] tamaños = codiSep[2].split(" ");
        return tamaños;
    }

    private String[][] pixelsMaker(String codiPPM) {
        String[] codiSep = codiPPM.split("\n");
        String[] tamaños = codiSep[2].split(" ");
        int nPixels = Integer.parseInt(tamaños[0])*Integer.parseInt(tamaños[1]);
        String[][] pixels =new String[nPixels][3];
        int index = 4;
        for (int i = 0; i < nPixels; i++) {
            for (int j = 0; j < 3; j++) {
                pixels[i][j] = codiSep[index];
                index++;
            }
        }
        return pixels;
    }
}
