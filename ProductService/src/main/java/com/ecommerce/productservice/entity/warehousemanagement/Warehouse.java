package com.ecommerce.productservice.entity.warehousemanagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Warehouse{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer warehouseId;
    private String warehouseName;
    private String warehouseAddress;
    private String warehousePincode;
    private String warehouseEmail;
    private String serviceablePincodeZones;
    @OneToMany(mappedBy = "warehouse",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Inventory> inventory;


}
