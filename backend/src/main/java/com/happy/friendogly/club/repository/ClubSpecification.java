package com.happy.friendogly.club.repository;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.SizeType;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import java.util.Set;
import org.springframework.data.jpa.domain.Specification;

public class ClubSpecification {

    private Specification<Club> spec;

    private ClubSpecification() {
        spec = Specification.where(null);
    }

    public static ClubSpecification where() {
        return new ClubSpecification();
    }

    public ClubSpecification equalsProvince(String province) {
        return equalsAddressDetail(province, "province");
    }

    private ClubSpecification equalsAddressDetail(String province, String fieldName) {
        if (StringUtils.isBlank(province)) {
            return this;
        }
        spec = spec.and(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("address").get(fieldName), province)
        );
        return this;
    }

    public ClubSpecification hasGenders(Set<Gender> genders) {
        return addInClause(genders, "allowedGenders");
    }

    public ClubSpecification hasSizeTypes(Set<SizeType> sizeTypes) {
        return addInClause(sizeTypes, "allowedSizes");
    }

    private <T> ClubSpecification addInClause(Set<T> values, String fieldName) {
        if (values == null || values.isEmpty()) {
            return this;
        }

        spec = spec.and((Root<Club> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            root.fetch(fieldName, JoinType.LEFT);
            Path<T> path = root.joinSet(fieldName, JoinType.LEFT);
            CriteriaBuilder.In<T> inClause = criteriaBuilder.in(path);
            values.forEach(inClause::value);
            return inClause;
        });

        return this;
    }

    public ClubSpecification orderByCreatedAtDescAndIdAsc() {
        spec.and((root, query, criteriaBuilder) -> {
            Order orderByCreatedAt = criteriaBuilder.desc(root.get("createdAt"));
            Order orderByIdAsc = criteriaBuilder.asc(root.get("id"));
            query.orderBy(orderByCreatedAt, orderByIdAsc);
            return criteriaBuilder.conjunction();
        });

        return this;
    }

    public Specification<Club> build() {
        return spec;
    }

}
