-- 修复customers表结构
-- 删除多余的customer_name字段，只保留name字段

-- 首先检查表结构
\d customers;

-- 删除customer_name字段（如果存在）
ALTER TABLE customers DROP COLUMN IF EXISTS customer_name;

-- 确保name字段存在且不为空
ALTER TABLE customers ALTER COLUMN name SET NOT NULL;

-- 检查最终表结构
\d customers; 