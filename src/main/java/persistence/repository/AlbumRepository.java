package persistence.repository;

import org.example.model.Album;
import org.example.model.Artist;
import javax.persistence.EntityManager;
import java.util.List;

public class AlbumRepository {
    private final EntityManager em;

    public AlbumRepository(EntityManager em) {
        this.em = em;
    }

    public Album save(Album album) {
        em.persist(album);
        return album;
    }

    public Album findById(Long id) {
        return em.find(Album.class, id);
    }

    public List<Album> findAll() {
        return em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
    }

    public List<Album> findByArtistId(Long artistId) {
        return em.createQuery("SELECT a FROM Album a WHERE a.artist.id = :artistId", Album.class)
            .setParameter("artistId", artistId)
            .getResultList();
    }
}
