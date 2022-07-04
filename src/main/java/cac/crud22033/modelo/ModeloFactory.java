package cac.crud22033.modelo;

/**
 *
 * @author Charly Cimino Aprendé más Java en mi canal:
 * https://www.youtube.com/c/CharlyCimino Encontrá más código en mi repo de
 * GitHub: https://github.com/CharlyCimino
 */
public class ModeloFactory {

    private ModeloFactory() {}
    
    private static ModeloFactory mf = null;

    public static ModeloFactory getInstance() {
        if (mf == null) {
            mf = new ModeloFactory();
        }
        return mf;
    }

    public Modelo crearModelo(String nombreModelo) {
        Modelo m = null;
        String nombreClase = mf.getClass().getPackage().getName() + ".Modelo" + nombreModelo;
        try {
            m = (Modelo) Class.forName(nombreClase).getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Ocurrió un error al instanciar un modelo de tipo " + nombreClase, ex);
        }
        return m;
    }
}
