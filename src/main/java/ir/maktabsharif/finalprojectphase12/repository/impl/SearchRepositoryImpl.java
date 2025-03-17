package ir.maktabsharif.finalprojectphase12.repository.impl;

import ir.maktabsharif.finalprojectphase12.entity.User;
import ir.maktabsharif.finalprojectphase12.entity.enums.ApprovalStatus;
import ir.maktabsharif.finalprojectphase12.entity.enums.Role;
import ir.maktabsharif.finalprojectphase12.repository.SearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class SearchRepositoryImpl implements SearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> searchUsers(Role role, String firstname, String lastname, ApprovalStatus approvalStatus) {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> user = query.from(User.class);

            List<Predicate> predicates = new ArrayList<>();

            if (role != null) {
                predicates.add(cb.equal(user.get("role"), role));
            }
            if (firstname != null && !firstname.isEmpty()) {
                predicates.add(cb.like(cb.lower(user.get("firstName")), firstname.toLowerCase() + "%"));
            }
            if (lastname != null && !lastname.isEmpty()) {
                predicates.add(cb.like(cb.lower(user.get("lastName")), lastname.toLowerCase() + "%"));
            }
            if (approvalStatus != null) {
                predicates.add(cb.equal(user.get("approvalStatus"), approvalStatus));
            }

            query.where(predicates.toArray(new Predicate[0]));

            return entityManager.createQuery(query).getResultList();
        }
    }

