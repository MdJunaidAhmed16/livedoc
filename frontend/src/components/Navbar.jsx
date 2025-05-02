import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { toast } from "react-toastify";

function Navbar() {
  const { accessToken, logout } = useAuth();
  const navigate = useNavigate();

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
    <nav className="w-full flex items-center justify-between p-4 bg-white shadow-md">
      <div className="text-2xl font-bold text-purple-600">
        <Link to="/">LiveDoc</Link>
      </div>

      {accessToken && (
        <div className="flex space-x-4 items-center">
          <Link to="/" className="text-gray-700 hover:text-purple-600 font-medium">
            Home
          </Link>
          <button
            onClick={handleLogout}
            className="py-1 px-4 bg-purple-500 hover:bg-purple-600 text-white font-semibold rounded-lg transition duration-300"
          >
            Logout
          </button>
        </div>
      )}
    </nav>
  );
}

export default Navbar;
