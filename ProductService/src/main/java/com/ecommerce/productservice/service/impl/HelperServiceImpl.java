package com.ecommerce.productservice.service.impl;

import com.ecommerce.productservice.dto.response.DeliveryTimeDetails;
import com.ecommerce.productservice.dto.response.DeliveryTimeResponse;
import com.ecommerce.productservice.entity.PincodeDetails;
import com.ecommerce.productservice.entity.warehousemanagement.Inventory;
import com.ecommerce.productservice.entity.warehousemanagement.Warehouse;
import com.ecommerce.productservice.exception.ErrorCode;
import com.ecommerce.productservice.exception.ProductException;
import com.ecommerce.productservice.repository.InventoryRepo;
import com.ecommerce.productservice.repository.PincodeRepo;
import com.ecommerce.productservice.service.declaration.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class HelperServiceImpl implements HelperService {

    @Value("${api.secret}")
    private String apiSecret;

    @Autowired
    PincodeRepo pincodeRepo;
    @Autowired
    InventoryRepo inventoryRepo;

    @Value("${AVAILABILITY_SUCCESS_MESSAGE}")
    String AVAILABILITY_SUCCESS_MESSAGE;

    private static final double EARTH_RADIUS_KM = 6371;


    @Override
    public DeliveryTimeResponse getDeliveryAvailability(String pincode, String skuId) throws ProductException {

        List<Inventory> inventory = inventoryRepo.findBySkuId(skuId);
        List<DeliveryTimeDetails> timeDetailsList = new ArrayList<>();
        if (!inventory.isEmpty() && inventoryRepo.SumQuantityBySizeVariantId(skuId) > 0) {
            List<Warehouse> warehouseList = new ArrayList<>();
            inventory.forEach(inv -> warehouseList.add(inv.getWarehouse()));
            Map<Warehouse, Integer> deliveryTimeMap = distanceFromWarehouse(pincode, warehouseList);

            if (!deliveryTimeMap.isEmpty()) {
                deliveryTimeMap.forEach((warehouse, time) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_MONTH, time);
                    String response = AVAILABILITY_SUCCESS_MESSAGE + cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()) + ", "
                            + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)) + " " + cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());

                    timeDetailsList.add(new DeliveryTimeDetails(response,time.toString(), warehouse));
                });

            } else {
                return new DeliveryTimeResponse(false, null);
            }
        } else {
            return new DeliveryTimeResponse(false, null);
        }
        return new DeliveryTimeResponse(true, timeDetailsList);
    }

    @Override
    public Boolean validateApiKey(String secret) throws ProductException {
        if (secret.equals(apiSecret))
            return true;
        else
            throw new ProductException(ErrorCode.INVALID_API_SECRET.name());
    }

    public Map<Warehouse, Integer> distanceFromWarehouse(String pincode, List<Warehouse> warehouseList) throws ProductException {

        Map<Warehouse, Integer> deliveryTimeMap = new HashMap<>();
        try {
            warehouseList.forEach(warehouse -> {
                AtomicBoolean flag = new AtomicBoolean(false);
                List.of(warehouse.getServiceablePincodeZones().split(","))
                        .forEach(s -> {
                            if (pincode.startsWith(s))
                                flag.set(true);
                        });
                if (flag.get()) {
                    PincodeDetails to = pincodeRepo.findById(warehouse.getWarehousePincode()).get();
                    PincodeDetails from = pincodeRepo.findById(pincode).get();
                    float distance = distance(to.getLatitude(), to.getLongitude(), from.getLatitude(), from.getLongitude());
                    Integer timeToDeliver = Math.round(distance / 150);
                    deliveryTimeMap.put(warehouse, timeToDeliver);
                }
            });
        }catch (NoSuchElementException ex){
            throw new ProductException(ErrorCode.NOT_SERVICEABLE_PINCODE.name());
        }
        if (deliveryTimeMap.isEmpty())
            return deliveryTimeMap;
        return getSortedMap(deliveryTimeMap);
    }

    public Map<Warehouse, Integer> getSortedMap(Map<Warehouse, Integer> map) {
        return map.entrySet()       // Get the set of entries from the map
                .stream()                             // Create a stream from the set of entries
                .sorted(Map.Entry.comparingByValue()) // Sort the entries by their values
                .collect(Collectors.toMap(            // Collect the sorted entries into a new map
                        Map.Entry::getKey,            // The key of the new map entry will be the key of the original map entry
                        Map.Entry::getValue,          // The value of the new map entry will be the value of the original map entry
                        (e1, e2) -> e1,               // In case of a key collision, keep the first entry (this shouldn't happen in this context)
                        LinkedHashMap::new));
    }

    public static double degreesToRadians(double degrees) {
        return degrees * (Math.PI / 180);
    }

    public static float distance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = degreesToRadians(lat1);
        double lat2Rad = degreesToRadians(lat2);
        double lon1Rad = degreesToRadians(lon1);
        double lon2Rad = degreesToRadians(lon2);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = Math.round(EARTH_RADIUS_KM * c);
        return (float) (distance + distance * ((double) 20 / 100));
    }
}

