package com.backend.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String name;

    private int level;

    // Moi quan he de quy, Moi quan he tu tham chieu
    // Nó cho phép biểu diễn cấu trúc cây hoặc đồ thị trong dữ liệu.
    // Trong trường hợp này, nó có thể được sử dụng để biểu diễn cấu trúc danh mục
    // có các danh mục con và danh mục cha.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

}
