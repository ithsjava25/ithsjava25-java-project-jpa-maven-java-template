package persistence.repository;

import org.example.model.Album;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class AlbumRepository {
    private final EntityManager em;

    public AlbumRepository(EntityManager em) {
        this.em = em;
    }

    public Album save(Album album) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(album);
            transaction.commit();
            return album;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
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

    public boolean deleteAlbum(Long id) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Album album = em.find(Album.class, id);
            if (album != null) {
                em.remove(album);
                transaction.commit();
                return true;
            }
            transaction.rollback();
            return false;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            return false;
        }
    }
}
