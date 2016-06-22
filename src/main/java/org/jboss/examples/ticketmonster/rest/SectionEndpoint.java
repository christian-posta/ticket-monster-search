package org.jboss.examples.ticketmonster.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.jboss.examples.ticketmonster.rest.dto.SectionDTO;
import org.jboss.examples.ticketmonster.model.Section;

/**
 * 
 */
@Stateless
@Path("/sections")
public class SectionEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;



   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<Section> findByIdQuery = em.createQuery("SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue WHERE s.id = :entityId ORDER BY s.id", Section.class);
      findByIdQuery.setParameter("entityId", id);
      Section entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      SectionDTO dto = new SectionDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<SectionDTO> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
   {
      TypedQuery<Section> findAllQuery = em.createQuery("SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue ORDER BY s.id", Section.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<Section> searchResults = findAllQuery.getResultList();
      final List<SectionDTO> results = new ArrayList<SectionDTO>();
      for (Section searchResult : searchResults)
      {
         SectionDTO dto = new SectionDTO(searchResult);
         results.add(dto);
      }
      return results;
   }


}
