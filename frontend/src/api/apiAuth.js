import ky from "ky";

const apiAuth = ky.create({
  prefixUrl: "http://172.191.132.105:9091",
  headers: () => {
    const token = localStorage.getItem("token"); // ✅ Obtener token
    return {
      "Content-Type": "application/json",
      Authorization: token ? `Bearer ${token}` : "",
    };
  },
});

export default apiAuth;
