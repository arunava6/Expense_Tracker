import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function OAuth2RedirectHandler() {
    const { loginWithToken } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        // Helper to get cookie by name
        const getCookie = (name) => {
            const value = `; ${document.cookie}`;
            const parts = value.split(`; ${name}=`);
            if (parts.length === 2) return parts.pop().split(';').shift();
        };

        const token = getCookie('accessToken');

        if (token) {
            loginWithToken(token);
            // Clear the cookie by setting expiry to past
            document.cookie = "accessToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            navigate('/');
        } else {
            console.error('No access token found in cookies');
            navigate('/login?error=oauth_failed');
        }
    }, [loginWithToken, navigate]);

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50">
            <div className="text-gray-600">Processing login...</div>
        </div>
    );
}
