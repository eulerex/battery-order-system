// 组件加载器
class ComponentLoader {
    static async loadComponent(elementId, componentPath) {
        try {
            const response = await fetch(componentPath);
            const html = await response.text();
            document.getElementById(elementId).innerHTML = html;
            
            // 执行组件中的脚本
            const scripts = document.getElementById(elementId).querySelectorAll('script');
            scripts.forEach(script => {
                if (script.textContent) {
                    eval(script.textContent);
                }
            });
        } catch (error) {
            console.error('加载组件失败:', error);
        }
    }
    
    static loadSidebar() {
        return this.loadComponent('sidebar-container', 'components/sidebar.html');
    }
}

// 页面初始化
document.addEventListener('DOMContentLoaded', function() {
    // 加载侧边栏
    ComponentLoader.loadSidebar();
    
    // 添加移动端菜单按钮
    const topBar = document.querySelector('.top-bar');
    if (topBar && !document.getElementById('menu-toggle')) {
        const menuToggle = document.createElement('button');
        menuToggle.id = 'menu-toggle';
        menuToggle.className = 'menu-toggle';
        menuToggle.innerHTML = '<i class="fas fa-bars"></i>';
        menuToggle.style.cssText = `
            display: none;
            background: none;
            border: none;
            font-size: 1.2rem;
            color: #333;
            cursor: pointer;
            padding: 8px;
            border-radius: 4px;
        `;
        
        // 在移动端显示菜单按钮
        if (window.innerWidth <= 768) {
            menuToggle.style.display = 'block';
        }
        
        topBar.insertBefore(menuToggle, topBar.firstChild);
    }
});

// 响应式处理
window.addEventListener('resize', function() {
    const menuToggle = document.getElementById('menu-toggle');
    if (menuToggle) {
        if (window.innerWidth <= 768) {
            menuToggle.style.display = 'block';
        } else {
            menuToggle.style.display = 'none';
            // 在桌面端自动关闭移动端菜单
            const sidebar = document.querySelector('.sidebar');
            if (sidebar) {
                sidebar.classList.remove('open');
            }
        }
    }
}); 