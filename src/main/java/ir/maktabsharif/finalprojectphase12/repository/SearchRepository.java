package ir.maktabsharif.finalprojectphase12.repository;

import ir.maktabsharif.finalprojectphase12.entity.User;
import ir.maktabsharif.finalprojectphase12.entity.enums.ApprovalStatus;
import ir.maktabsharif.finalprojectphase12.entity.enums.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository {
        List<User> searchUsers(Role role, String firstname, String lastname, ApprovalStatus approvalStatus);
}
