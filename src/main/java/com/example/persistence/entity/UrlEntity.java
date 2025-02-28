package com.example.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "url")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UrlEntity {
        @Id
        @Column(name = "id")
        private String id;

        @Column(name = "long_url")
        private String longUrl;
}
