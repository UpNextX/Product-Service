
INSERT INTO brands (id, name) VALUES (1, 'AMD');
INSERT INTO brands (id, name) VALUES (2, 'Intel');
INSERT INTO brands (id, name) VALUES (3, 'NVIDIA');
INSERT INTO brands (id, name) VALUES (4, 'ASUS');
INSERT INTO brands (id, name) VALUES (5, 'MSI');
INSERT INTO brands (id, name) VALUES (6, 'Corsair');
INSERT INTO brands (id, name) VALUES (7, 'NZXT');
INSERT INTO brands (id, name) VALUES (8, 'Seasonic');


INSERT INTO categories (id, name) VALUES (1, 'CPU');
INSERT INTO categories (id, name) VALUES (2, 'Motherboard');
INSERT INTO categories (id, name) VALUES (3, 'GPU');
INSERT INTO categories (id, name) VALUES (4, 'RAM');
INSERT INTO categories (id, name) VALUES (5, 'PC Case');
INSERT INTO categories (id, name) VALUES (6, 'PSU');


INSERT INTO products (id, name, price, description, image_url, stock, category_id, brand_id)
VALUES (1, 'Ryzen 5 3600', 199.99, '6-core, 12-thread processor', 'url1', 15, 1, 1);

INSERT INTO cpus (id, socket, cores, threads, base_clockghz, boost_clockghz, tdpw, max_memory_speedmhz, generation)
VALUES (1, 'AM4', 6, 12, 3.6, 4.2, 65, 3200, 'Zen 2');


INSERT INTO cpu_supported_memory_types (cpu_id, supported_memory_types)
VALUES (1, 'DDR4');

INSERT INTO products (id, name, price, description, image_url, stock, category_id, brand_id)
VALUES (2, 'Ryzen 7 3700X', 299.99, '8-core, 16-thread processor', 'url2', 12, 1, 1);

INSERT INTO cpus (id, socket, cores, threads, base_clockghz, boost_clockghz, tdpw, max_memory_speedmhz, generation)
VALUES (2, 'AM4', 8, 16, 3.6, 4.4, 65, 3200, 'Zen 2');

INSERT INTO cpu_supported_memory_types (cpu_id, supported_memory_types) VALUES (2, 'DDR4');


INSERT INTO products (id, name, price, description, image_url, stock, category_id, brand_id)
VALUES (3, 'ASUS TUF Gaming B450-PLUS', 129.99, 'ATX motherboard', 'url3', 8, 2, 4);

INSERT INTO motherboards (id, socket, chipset, form_factor, ram_type, ram_slots, max_memory_speedmhz, pcie_version, m2slots, wifi)
VALUES (3, 'AM4', 'B450', 'ATX', 'DDR4', 4, 4400, 'PCIe 3.0', 2, false);

INSERT INTO products (id, name, price, description, image_url, stock, category_id, brand_id)
VALUES (4, 'MSI X570-A PRO', 199.99, 'ATX motherboard', 'url4', 5, 2, 5);

INSERT INTO motherboards (id, socket, chipset, form_factor, ram_type, ram_slots, max_memory_speedmhz, pcie_version, m2slots, wifi)
VALUES (4, 'AM4', 'X570', 'ATX', 'DDR4', 4, 4400, 'PCIe 4.0', 2, true);


INSERT INTO products (id, name, price, description, image_url, stock, category_id, brand_id)
VALUES (5, 'NVIDIA RTX 3060', 329.99, 'Mid-range gaming GPU', 'url5', 10, 3, 3);

INSERT INTO gpus (id, vramgb, tdpw, recommendedpsuwatt, performance_tier, length_mm)
VALUES (5, 12, 170, 550, 'mid', 242);

INSERT INTO products (id, name, price, description, image_url, stock, category_id, brand_id)
VALUES (6, 'NVIDIA RTX 4070', 599.99, 'High-end gaming GPU', 'url6', 7, 3, 3);

INSERT INTO gpus (id, vramgb, tdpw, recommendedpsuwatt, performance_tier, length_mm)
VALUES (6, 12, 200, 650, 'high', 267);


INSERT INTO products (id, name, price, description, image_url, stock, category_id, brand_id)
VALUES (7, 'Corsair Vengeance 16GB', 79.99, '16GB DDR4 RAM', 'url7', 20, 4, 6);

INSERT INTO ram_kits (id, type, capacitygb, modules, speedmhz, cas_latency, ecc)
VALUES (7, 'DDR4', 16, 2, 3200, 16, false);

INSERT INTO products (id, name, price, description, image_url, stock, category_id, brand_id)
VALUES (8, 'G.Skill Ripjaws 32GB', 159.99, '32GB DDR4 RAM', 'url8', 15, 4, 6);

INSERT INTO ram_kits (id, type, capacitygb, modules, speedmhz, cas_latency, ecc)
VALUES (8, 'DDR4', 32, 2, 3600, 18, false);


INSERT INTO products (id, name, price, description, image_url, stock, category_id, brand_id)
VALUES (9, 'NZXT H510', 99.99, 'Mid-tower ATX case', 'url9', 10, 5, 7);

INSERT INTO pc_cases (id, max_gpu_length_mm, max_cpu_cooler_height_mm, psu_form_factor)
VALUES (9, 381, 165, 'ATX');

INSERT INTO pccase_form_factor_support (pccase_id, form_factor_support) VALUES (9, 'ATX');
INSERT INTO pccase_form_factor_support (pccase_id, form_factor_support) VALUES (9, 'Micro-ATX');
INSERT INTO pccase_form_factor_support (pccase_id, form_factor_support) VALUES (9, 'Mini-ITX');


INSERT INTO products (id, name, price, description, image_url, stock, category_id, brand_id)
VALUES (10, 'Seasonic Focus 650W', 109.99, '650W 80+ Gold Fully Modular PSU', 'url10', 8, 6, 8);

INSERT INTO psus (id, wattage, efficiency, modularity, form_factor)
VALUES (10, 650, '80+ Gold', 'Fully Modular', 'ATX');
