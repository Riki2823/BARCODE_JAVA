public class Image {
    String codiPPM;
    String[][] pixels;
    String altura;
    String ancho;

    Image(String codiPPM){
        this.codiPPM = codiPPM;
        this.pixels = pixelsMaker(codiPPM);
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
