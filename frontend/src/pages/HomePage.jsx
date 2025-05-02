import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { jwtDecode } from "jwt-decode"; // ✅ Correct way for new jwt-decode versions


function HomePage() {
  const { accessToken, logout } = useAuth();
  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState({ username: "", role: "" });

  useEffect(() => {
    if (!accessToken) {
      toast.info("Please login to continue");
      navigate("/login");
    } else {
      const decoded = jwtDecode(accessToken);
      setUserInfo({
        username: decoded.username || "",
        role: decoded.role || "",
      });
    }
  }, [accessToken, navigate]);

  const handleLogout = async () => {
    try {
      await fetch("http://localhost:8080/api/auth/logout", {
        method: "POST",
        credentials: "include",
      });

      logout();
      toast.success("Logged out successfully!");
      navigate("/login");
    } catch (error) {
      console.error(error);
      toast.error("Logout failed!");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-r from-teal-200 via-cyan-200 to-blue-200 p-4">
      <div className="bg-white shadow-2xl rounded-2xl p-10 max-w-xl w-full text-center animate-fadeIn">
        <h1 className="text-4xl font-bold text-gray-800 mb-4">
          Welcome back, {userInfo.username} 👋
        </h1>
        <p className="text-gray-600 text-lg mb-2">
          Role: <span className="font-semibold">{userInfo.role}</span>
        </p>
        <p className="text-gray-500 mb-8">
          Ready to collaborate and create documents?
        </p>

        <button
          onClick={() => toast.info("Feature coming soon! 🚀")}
          className="py-2 px-6 bg-purple-500 hover:bg-purple-600 text-white font-semibold rounded-xl transition duration-300 mb-4"
        >
          + New Document
        </button>

        <div className="mt-6">
          <button
            onClick={handleLogout}
            className="py-2 px-6 bg-red-500 hover:bg-red-600 text-white font-semibold rounded-xl transition duration-300"
          >
            Logout
          </button>
        </div>
      </div>
    </div>
  );
}

export default HomePage;
