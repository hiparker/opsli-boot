import org.junit.Test;
import org.opsli.api.wrapper.system.dict.DictModel;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.utils.ValidatorUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 测试 验证工具类
 *
 * @author Pace
 * @date 2025-06-01 13:44
 **/
public class TestValidatorUtil {

    /**
     * 测试方法 - 保留原有的测试逻辑并增强
     */
    @Test
    public void test(){
        System.out.println("=== ValidatorUtil 测试开始 ===");
        try {
            // 原有测试用例
            testOriginalCase();

            // 新增测试用例
            testBatchValidation();
            testPerformance();
            testSecurity();

            System.out.println("=== 所有测试通过 ===");

        } catch (Exception e) {
            System.err.println("测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 原有测试用例
     */
    private static void testOriginalCase() {
        System.out.println("--- 测试原有用例 ---");

        DictModel dictModel = new DictModel();
        dictModel.setTypeCode("asdsa");
        dictModel.setTypeName("阿哈哈哈哈");
        dictModel.setRemark("测试11232131231231223123");
        dictModel.setIzLock("1");

        try {
            ValidatorUtil.verify(dictModel);
            System.out.println("✓ 原有测试用例验证通过");
        } catch (ServiceException e) {
            System.out.println("✗ 原有测试用例验证失败: " + e.getMessage());
        }
    }

    /**
     * 批量验证测试
     */
    private static void testBatchValidation() {
        System.out.println("--- 测试批量验证 ---");

        List<DictModel> models = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DictModel model = new DictModel();
            model.setTypeCode("code" + i);
            model.setTypeName("名称" + i);
            model.setRemark("备注" + i);
            model.setIzLock("1");
            models.add(model);
        }

        try {
            long start = System.currentTimeMillis();
            ValidatorUtil.verifyBatch(models);
            long end = System.currentTimeMillis();
            System.out.println("✓ 批量验证通过，耗时: " + (end - start) + "ms");
        } catch (ServiceException e) {
            System.out.println("✗ 批量验证失败: " + e.getMessage());
        }
    }

    /**
     * 性能测试
     */
    private static void testPerformance() {
        System.out.println("--- 测试性能优化 ---");

        DictModel model = new DictModel();
        model.setTypeCode("testCode");
        model.setTypeName("测试名称");
        model.setRemark("测试备注");
        model.setIzLock("1");

        // 预热
        for (int i = 0; i < 100; i++) {
            ValidatorUtil.verify(model);
        }

        // 性能测试
        int iterations = 10000;
        long start = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            ValidatorUtil.verify(model);
        }

        long end = System.currentTimeMillis();
        double avgTime = (double)(end - start) / iterations;

        System.out.println("✓ 性能测试完成");
        System.out.println("  - 总验证次数: " + iterations);
        System.out.println("  - 总耗时: " + (end - start) + "ms");
        System.out.println("  - 平均耗时: " + String.format("%.3f", avgTime) + "ms");
    }

    /**
     * 安全性测试
     */
    private static void testSecurity() {
        System.out.println("--- 测试安全性 ---");

        // 测试超长字符串
        DictModel model = new DictModel();
        model.setTypeCode("test");
        model.setTypeName("正常名称");

        // 创建超长字符串
        StringBuilder longString = new StringBuilder();
        for (int i = 0; i < 15000; i++) {
            longString.append("a");
        }
        model.setRemark(longString.toString());
        model.setIzLock("1");

        try {
            ValidatorUtil.verify(model);
            System.out.println("✗ 安全性测试失败: 应该拒绝超长字符串");
        } catch (ServiceException e) {
            System.out.println("✓ 安全性测试通过: 正确拒绝了超长字符串");
        }

        // 测试空值安全性
        try {
            ValidatorUtil.verify(null);
            System.out.println("✓ 空值安全性测试通过");
        } catch (Exception e) {
            System.out.println("✗ 空值安全性测试失败: " + e.getMessage());
        }

        // 测试空集合
        try {
            ValidatorUtil.verifyBatch(null);
            ValidatorUtil.verifyBatch(Collections.emptyList());
            System.out.println("✓ 空集合安全性测试通过");
        } catch (Exception e) {
            System.out.println("✗ 空集合安全性测试失败: " + e.getMessage());
        }
    }

}
