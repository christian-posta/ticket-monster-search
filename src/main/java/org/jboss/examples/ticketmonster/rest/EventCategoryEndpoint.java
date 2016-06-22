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
import org.jboss.examples.ticketmonster.rest.dto.EventCategoryDTO;
import org.jboss.examples.ticketmonster.model.EventCategory;

/**
 * 
 */
@Stateless
@Path("/eventcategories")
public class EventCategoryEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;


   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<EventCategory> findByIdQuery = em.createQuery("SELECT DISTINCT e FROM EventCategory e WHERE e.id = :entityId ORDER BY e.id", EventCategory.class);
      findByIdQuery.setParameter("entityId", id);
      EventCategory entity;
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
      EventCategoryDTO dto = new EventCategoryDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<EventCategoryDTO> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
   {
      TypedQuery<EventCategory> findAllQuery = em.createQuery("SELECT DISTINCT e FROM EventCategory e ORDER BY e.id", EventCategory.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<EventCategory> searchResults = findAllQuery.getResultList();
      final List<EventCategoryDTO> results = new ArrayList<EventCategoryDTO>();
      for (EventCategory searchResult : searchResults)
      {
         EventCategoryDTO dto = new EventCategoryDTO(searchResult);
         results.add(dto);
      }
      return results;
   }


}
