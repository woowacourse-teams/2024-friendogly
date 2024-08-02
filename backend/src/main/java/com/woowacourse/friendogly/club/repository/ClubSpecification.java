package com.woowacourse.friendogly.club.repository;

import com.woowacourse.friendogly.club.domain.Club;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
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

    public ClubSpecification equalsCity(String city) {
        return equalsAddressDetail(city, "city");
    }

    public ClubSpecification equalsVillage(String village) {
        return equalsAddressDetail(village, "village");
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

    public Specification<Club> build() {
        return spec;
    }

}
