package ar.edu.unlam.tallerweb1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unlam.tallerweb1.modelo.Ciudad;
import ar.edu.unlam.tallerweb1.modelo.Continente;
import ar.edu.unlam.tallerweb1.modelo.Pais;
import ar.edu.unlam.tallerweb1.modelo.Ubicacion;

public class Tests extends SpringTest {

    @Test
    @Transactional
    @Rollback(true)
    public void testetodoslospaísesdehablainglesa() {
        Pais pais1=new Pais("EEUU", 3456,"Ingles");
        Pais pais2=new Pais("Chile", 3456,"Español");
        Pais pais3=new Pais("Canadá", 3456,"Ingles");

        getSession().save(pais1);
        getSession().save(pais2);
        getSession().save(pais3);
        List<Pais> paisesDeHablaInglesa= getSession().createCriteria(Pais.class)
                .add(Restrictions.eq("idioma","Ingles"))
                .list();

        assertEquals(paisesDeHablaInglesa.size(), 2);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testtodoslospaísesdelcontinenteeuropeo() {
        Continente continente1=new Continente("Europa");
        Continente continente2=new Continente("America");
        getSession().save(continente1);
        getSession().save(continente2);
        Pais pais1=new Pais("EEUU", 3456,"Ingles");
        Pais pais2=new Pais("España", 3456,"Español");
        Pais pais3=new Pais("Canadá", 3456,"Ingles");
        pais1.setContinente(continente2);
        pais2.setContinente(continente1);
        pais3.setContinente(continente2);

        getSession().save(pais1);
        getSession().save(pais2);
        getSession().save(pais3);
        List<Pais> paisesEuropeos= getSession().createCriteria(Pais.class)
                .createAlias("continente", "continenteBuscado")
                .add(Restrictions.eq("continenteBuscado.nombre","Europa"))
                .list();
        assertEquals(paisesEuropeos.size(),1);

    }

    @Test
    @Transactional
    @Rollback(true)
    public void testtodoslospaísescuyacapitalestánalnortedeltrópicodecáncer() {

        Pais pais1=new Pais("EEUU", 3456,"Ingles");
        Pais pais2=new Pais("Chile", 3456,"Español");
        Ciudad ciudad1=new Ciudad("Washington");
        Ubicacion ubicacion1=new Ubicacion(38.00,-97.00); //latitud longitud
        getSession().save(ubicacion1);
        ciudad1.setUbicacion(ubicacion1);

        Ciudad ciudad2=new Ciudad("Santiago de chile");
        Ubicacion ubicacion2=new Ubicacion(-33.45, -70.64);
        getSession().save(ubicacion2);
        ciudad2.setUbicacion(ubicacion2);

        getSession().save(ciudad1);
        getSession().save(ciudad2);

        pais1.setCapital(ciudad1);
        pais2.setCapital(ciudad2);

        getSession().save(pais1);
        getSession().save(pais2);

        List<Pais> paises= getSession().createCriteria(Pais.class)
                .createAlias("capital", "capitalBuscada")
                .createAlias("capitalBuscada.ubicacion","ubicacionBuscada")
                .add(Restrictions.gt("ubicacionBuscada.latitud", 23.5))
                .list();

        assertEquals(paises.size(),1);

    }

    @Test
    @Transactional
    @Rollback(true)
    public void testtodaslasciudadesdelhemisferiosur() {


        Ciudad ciudad1=new Ciudad("Washington");
        Ubicacion ubicacion1=new Ubicacion(38.00,-97.00); //latitud longitud
        getSession().save(ubicacion1);
        ciudad1.setUbicacion(ubicacion1);

        Ciudad ciudad2=new Ciudad("Santiago de chile");
        Ubicacion ubicacion2=new Ubicacion(-33.45, -70.64);
        getSession().save(ubicacion2);
        ciudad2.setUbicacion(ubicacion2);

        getSession().save(ciudad1);
        getSession().save(ciudad2);


        List<Ciudad> ciudades= getSession().createCriteria(Ciudad.class)
                .createAlias("ubicacion","ubicacionBuscada")
                .add(Restrictions.lt("ubicacionBuscada.latitud", 0.0))
                .list();

        assertEquals(ciudades.size(),1);

    }


}
