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
import org.jboss.examples.ticketmonster.rest.dto.SectionAllocationDTO;
import org.jboss.examples.ticketmonster.model.SectionAllocation;

/**
 * 
 */
@Stateless
@Path("/sectionallocations")
public class SectionAllocationEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;



   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<SectionAllocation> findByIdQuery = em.createQuery("SELECT DISTINCT s FROM SectionAllocation s LEFT JOIN FETCH s.performance LEFT JOIN FETCH s.section WHERE s.id = :entityId ORDER BY s.id", SectionAllocation.class);
      findByIdQuery.setParameter("entityId", id);
      SectionAllocation entity;
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
      SectionAllocationDTO dto = new SectionAllocationDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<SectionAllocationDTO> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
   {
      TypedQuery<SectionAllocation> findAllQuery = em.createQuery("SELECT DISTINCT s FROM SectionAllocation s LEFT JOIN FETCH s.performance LEFT JOIN FETCH s.section ORDER BY s.id", SectionAllocation.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<SectionAllocation> searchResults = findAllQuery.getResultList();
      final List<SectionAllocationDTO> results = new ArrayList<SectionAllocationDTO>();
      for (SectionAllocation searchResult : searchResults)
      {
         SectionAllocationDTO dto = new SectionAllocationDTO(searchResult);
         results.add(dto);
      }
      return results;
   }


}
