/* src/config.h.in.  Generated from configure.ac by autoheader.  */

/* Define if building universal (internal helper macro) */
#undef AC_APPLE_UNIVERSAL_BUILD

/* Host processor clips on negative float to int conversion. */
#define CPU_CLIPS_NEGATIVE

/* Host processor clips on positive float to int conversion. */
#define CPU_CLIPS_POSITIVE

/* Host processor is big endian. */
#define CPU_IS_BIG_ENDIAN

/* Host processor is little endian. */
#undef CPU_IS_LITTLE_ENDIAN

/* Define to 1 if you have the `alarm' function. */
#undef HAVE_ALARM

/* Set to 1 if you have alsa */
#undef HAVE_ALSA

/* Define to 1 if you have the `calloc' function. */
#undef HAVE_CALLOC

/* Define to 1 if you have the `ceil' function. */
#undef HAVE_CEIL

/* Define to 1 if you have the <dlfcn.h> header file. */
#undef HAVE_DLFCN_H

/* Set to 1 if you have fftw3 */
#undef HAVE_FFTW3

/* Define to 1 if you have the `floor' function. */
#undef HAVE_FLOOR

/* Define to 1 if you have the `fmod' function. */
#undef HAVE_FMOD

/* Define to 1 if you have the `free' function. */
#undef HAVE_FREE

/* Define to 1 if you have the <inttypes.h> header file. */
#undef HAVE_INTTYPES_H

/* Define to 1 if you have the `lrint' function. */
#undef HAVE_LRINT

/* Define to 1 if you have the `lrintf' function. */
#undef HAVE_LRINTF

/* Define to 1 if you have the `malloc' function. */
#define HAVE_MALLOC 1

/* Define to 1 if you have the `memcpy' function. */
#define HAVE_MEMCPY 1

/* Define to 1 if you have the `memmove' function. */
#define HAVE_MEMMOVE 1

/* Define to 1 if you have the <memory.h> header file. */
#define HAVE_MEMORY_H 1

/* Define if you have signal SIGALRM. */
#undef HAVE_SIGALRM

/* Define to 1 if you have the `signal' function. */
#undef HAVE_SIGNAL

/* Set to 1 if you have libsndfile */
#define HAVE_SNDFILE 1

/* Define to 1 if you have the <stdint.h> header file. */
#define HAVE_STDINT_H 1

/* Define to 1 if you have the <stdlib.h> header file. */
#define HAVE_STDLIB_H 1

/* Define to 1 if you have the <strings.h> header file. */
#undef HAVE_STRINGS_H

/* Define to 1 if you have the <string.h> header file. */
#undef HAVE_STRING_H

/* Define to 1 if you have the <sys/stat.h> header file. */
#undef HAVE_SYS_STAT_H

/* Define to 1 if you have the <sys/times.h> header file. */
#undef HAVE_SYS_TIMES_H

/* Define to 1 if you have the <sys/types.h> header file. */
#undef HAVE_SYS_TYPES_H

/* Define to 1 if you have the <unistd.h> header file. */
#define HAVE_UNISTD_H 1

/* Define to the sub-directory where libtool stores uninstalled libraries. */
#undef LT_OBJDIR

/* Set to 1 if compiling for Win32 */
#define OS_IS_WIN32 1

/* Name of package */
#define PACKAGE "samplerate"

/* Define to the address where bug reports for this package should be sent. */
#undef PACKAGE_BUGREPORT

/* Define to the full name of this package. */
#undef PACKAGE_NAME

/* Define to the full name and version of this package. */
#undef PACKAGE_STRING

/* Define to the one symbol short name of this package. */
#undef PACKAGE_TARNAME

/* Define to the home page for this package. */
#undef PACKAGE_URL

/* Define to the version of this package. */
#undef PACKAGE_VERSION

/* The size of `double', as computed by sizeof. */
#define SIZEOF_DOUBLE

/* The size of `float', as computed by sizeof. */
#define SIZEOF_FLOAT

/* The size of `int', as computed by sizeof. */
#define SIZEOF_INT

/* The size of `long', as computed by sizeof. */
#define SIZEOF_LONG

/* Define to 1 if you have the ANSI C header files. */
#undef STDC_HEADERS

/* Version number of package */
#define VERSION 11

/* Define WORDS_BIGENDIAN to 1 if your processor stores words with the most
   significant byte first (like Motorola and SPARC, unlike Intel). */
#if defined AC_APPLE_UNIVERSAL_BUILD
# if defined __BIG_ENDIAN__
#  define WORDS_BIGENDIAN 1
# endif
#else
# ifndef WORDS_BIGENDIAN
#  undef WORDS_BIGENDIAN
# endif
#endif
