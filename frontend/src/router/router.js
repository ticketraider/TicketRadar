// Vuex 때 처럼 create* 함수를 제공한다.
import { createWebHistory, createRouter } from 'vue-router';

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('@/views/HomeView.vue'), // 동적 import
    },
    {
        path: '/event-list',
        name: 'EventList',
        component: () => import('@/views/EventListView.vue'),
    },
    {
        path: '/event-detail/:eventId',
        name: 'EventDetail',
        component: () => import('@/views/EventDetailView.vue'),
    },
    // {
    //     path: '/ticket-viewer', // 경로 설정
    //     name: 'TicketViewer', // 이름 설정
    //     component: () => import('@/views/TicketViewerView.vue'),
    // },
    // {
    //     path: '/myTicketList', // 경로 설정
    //     name: 'MyTicketList', // 이름 설정
    //     component: () => import('@/views/MyTicketListViewBack.vue'),
    //     meta: { requiresAuth: true } // 로그인 필요 여부를 메타 필드로 추가
    // },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/LoginView.vue'),
        meta: { requiresAuth: false } // 로그인 필요
    },
    {
        path: '/sign-up',
        name: 'SignUp',
        component: () => import('@/views/SignUpView.vue'),
    },
    {
        path: '/admin',
        name: 'BackOffice',
        component: () => import('@/views/BackOffice.vue'),
        meta: { requiresAuth: true, requiresAdmin: true } // 로그인 및 관리자 권한 필요
    },
    {
        path: '/my-page',
        component: () => import('@/views/MyPageView.vue'),
        children: [
            {
                path: 'userinfo',
                component: () => import('../components/mypage/UserInfoComponent.vue'),
            },
            {
                path: 'tickets',
                component: () => import('../components/mypage/MyTicketListComponent.vue'),
            },
            {
                path: 'reviews',
                component: () => import('../components/mypage/MyReviewListComponent.vue'),
            },
            {
                path: 'likes',
                component: () => import('../components/mypage/MyLikeListComponent.vue'),
            },
        ],
        meta: { requiresAuth: true } // 로그인 필요
    },

];

import {jwtDecode} from "jwt-decode";
// 라우터 생성
export const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 네비게이션 가드 설정
router.beforeEach((to, from, next) => {
    const isAuthenticated = localStorage.getItem('token'); // 토큰 가져오기

    if (to.path === '/login' && isAuthenticated) {
        // 이미 로그인한 사용자는 로그인 페이지에 접근할 수 없도록 리디렉션
        next('/');
        return;
    }

    if (to.meta.requiresAuth && !isAuthenticated) {
        // 인증이 필요한 페이지에 접근하려고 하는데, 로그인하지 않은 경우
        alert("로그인이 필요합니다.")
        next('/login');
        return; // 다음 페이지로 넘어가지 않음
    }

    if (to.meta.requiresAdmin && isAuthenticated) {
        // 관리자 권한이 필요한 페이지에 접근하려고 하는데, 관리자가 아닌 경우
        const token = jwtDecode(isAuthenticated); // 토큰 해독
        const userRole = token.role; // 사용자 역할 추출
        if (userRole !== 'ADMIN') {
            alert(`관리자만 접근할 수 있습니다. ${userRole}`)
            next('/'); // 권한이 없음을 나타내는 페이지로 리다이렉트
            return; // 다음 페이지로 넘어가지 않음
        }
    }
    next(); // 다음 페이지로 이동
});

// 생성한 라우터 내보내기
export default router;