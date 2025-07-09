-- 修复orders表结构
-- 将customer_name字段重命名为name

-- 首先检查表结构
\d orders;

-- 如果存在customer_name字段，则重命名为name
ALTER TABLE orders RENAME COLUMN customer_name TO name;

-- 如果不存在name字段，则添加name字段
-- ALTER TABLE orders ADD COLUMN name VARCHAR(100);

-- 确保name字段不为空（如果需要的话）
-- ALTER TABLE orders ALTER COLUMN name SET NOT NULL;

-- 检查最终表结构
\d orders; 