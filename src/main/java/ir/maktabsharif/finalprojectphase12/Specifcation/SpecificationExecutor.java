/*
package ir.maktabsharif.finalprojectphase12.Specifcation;

public class SpecificationExecutor {
    // UserRepository.java
    public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    }

    // UserSpecifications.java
    public class UserSpecifications {

        public static Specification<User> hasRole(Role role) {
            return (root, query, cb) -> {
                if (role == null) {
                    return null;
                }
                return cb.equal(root.get("role"), role);
            };
        }

        public static Specification<User> hasFirstName(String firstName) {
            return (root, query, cb) -> {
                if (firstName == null || firstName.isEmpty()) {
                    return null;
                }
                return cb.like(cb.lower(root.get("firstName")), firstName.toLowerCase() + "%");
            };
        }

        public static Specification<User> hasLastName(String lastName) {
            return (root, query, cb) -> {
                if (lastName == null || lastName.isEmpty()) {
                    return null;
                }
                return cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
            };
        }

        public static Specification<User> hasApprovalStatus(ApprovalStatus approvalStatus) {
            return (root, query, cb) -> {
                if (approvalStatus == null) {
                    return null;
                }
                return cb.equal(root.get("approvalStatus"), approvalStatus);
            };
        }

        public static Specification<User> buildSpecification(Role role, String firstName, String lastName, ApprovalStatus approvalStatus) {
            return Specification.where(hasRole(role))
                    .and(hasFirstName(firstName))
                    .and(hasLastName(lastName))
                    .and(hasApprovalStatus(approvalStatus));
        }
    }

    // SearchService.java
    @Service
    public class SearchService {

        @Autowired
        private UserRepository userRepository;

        public List<User> searchUsers(Role role, String firstName, String lastName, ApprovalStatus approvalStatus) {
            Specification<User> spec = UserSpecifications.buildSpecification(role, firstName, lastName, approvalStatus);
            return userRepository.findAll(spec);
        }
    }

}
*/
